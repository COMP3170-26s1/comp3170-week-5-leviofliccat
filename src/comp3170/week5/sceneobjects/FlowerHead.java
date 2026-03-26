package comp3170.week5.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class FlowerHead extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;

	private Vector3f petalColour = new Vector3f(1.0f,1.0f,1.0f);
	
	private float innerRadius = 0.3f;
	private float outerRadius = 0.5f;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	public FlowerHead(int nPetals, Vector3f colour) {
		
		// TODO: Create the flower head. (TASK 1)
		// Consider the best way to draw the mesh with the nPetals input. 
		// Note that this may involve moving some code OUT of this class!
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);		
		
		petalColour = colour;
		
		vertices = new Vector4f[nPetals*2 + 1];
		
		//make an inner circle?
		vertices[0] = new Vector4f(0, 0, 0, 1);
		Matrix4f rotate = new Matrix4f();

		for (int i = 0; i<nPetals*2; i++) {
			float angle = i * TAU / nPetals/2;
			rotate.rotationZ(angle); 
			float r = innerRadius;
			if (i%2 != 0) {
				r = outerRadius;
			}
			vertices[i + 1] = new Vector4f(r, 0, 0, 1); 
			vertices[i + 1].mul(rotate); 
			
			
		}
		
		
		vertexBuffer = GLBuffers.createBuffer(vertices);
		
		indices = new int[nPetals*2 * 3]; // each side creates 1 triangle with 3 vertices

		int k = 0;
		for (int i = 1; i <= nPetals*2; i++) {
			indices[k++] = 0;
			indices[k++] = i;
			indices[k++] = (i % (nPetals*2)) + 1; // wrap around when i = NSIDES
		}

		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	public void update(float dt) {
		// TODO: Make the flower head rotate. (TASK 5)
	}

	public void drawSelf(Matrix4f mvpMatrix) {
		// TODO: Add any appropriate draw code. (TASK 1)
		shader.enable();
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setUniform("u_colour", petalColour);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
