package helpers.camera;

import devengine.DisplayManager;
import entities.Player;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;

//public class TestCamera extends Camera
public class TestCamera
{
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private boolean FPS = false;

	private Vector3f position = new Vector3f(110, 10, -52);
	private float pitch = 20;
	private float yaw;
	private float roll;

	private static final float RUN_SPEED = 20;
	private static final float GRAVITY = -50f;
	private static final float JUMP_POWER = 30;

	private int clickedOnPlayer = 0;

	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	private Player player;

	public TestCamera(Player player)
	{
		this.player = player;
	}

	public void move(Terrain terrain)
	{
		while (Mouse.next())
		{
			if (Mouse.getEventButtonState())
			{
				if (Mouse.getX() >= 600 && Mouse.getX() <= 675)
				{
					if (Mouse.getY() >= 365 && Mouse.getY() <= 550)
					{
						clickedOnPlayer = 1;
					}
				}
			}
			else
			{
				if (Mouse.getEventButton() == 0)
				{
					if (clickedOnPlayer == 1)
					{
						if (Mouse.getX() >= 600 && Mouse.getX() <= 675)
						{
							if (Mouse.getY() >= 365 && Mouse.getY() <= 550)
							{
								FPS = true;
							}
						}
					}
				}
			}
		}

		if (FPS == false)
		{
			if (Mouse.isGrabbed())
			{
				Mouse.setGrabbed(false);
			}
			player.move(terrain);
			calculateZoom();
			calculatePitch();
			calculateAngleAroundPlayer();
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			calculateCameraPosition(horizontalDistance, verticalDistance);
			this.yaw = 180 - (player.getRotation().y + angleAroundPlayer);

		}
		else
		{

			Mouse.setGrabbed(true);
			this.yaw = (Mouse.getX());
			float MouseY = Mouse.getY();

			if(MouseY > 360)
			{
				pitch = 465 - MouseY;
			}

			if (yaw >= 1079)
			{
				Mouse.setCursorPosition(Display.getWidth() / 2 + 80,
						Display.getHeight() / 2);
				this.yaw = 720;
			}
			else if (yaw <= 359)
			{
				Mouse.setCursorPosition(Display.getWidth() / 2 + 80,
						Display.getHeight() / 2);
				this.yaw = 720;
			}

			if(pitch <= -100)
			{
				pitch = -100;
			}

			float theta = yaw - 720;

			float offsetX = (float) (2 * Math.sin(Math.toRadians(theta)));
			float offsetZ = (float) (2 * Math.cos(Math.toRadians(theta)));
			moveCamera();
			upwardsSpeed += GRAVITY * DisplayManager.getCurrentFrameDurationSeconds();
			position.y += upwardsSpeed * DisplayManager.getCurrentFrameDurationSeconds();
			player.getPosition().y += upwardsSpeed * DisplayManager.getCurrentFrameDurationSeconds();
//			float terrainHeight = terrain.getHeightOfTerain(player.getPosition().x, player.getPosition().z); // TODO add this to player class
			float terrainHeight = 0f;
			if (position.y - 10 <= terrainHeight)
			{
				upwardsSpeed = 0;
				isInAir = false;
				position.y = terrainHeight + 10;
				player.getPosition().y = terrainHeight;
			}

			player.getPosition().x = position.x - offsetX;
			player.getPosition().z = position.z + offsetZ;
			player.setRotationY(180 - theta);

			if (Mouse.isButtonDown(1))
			{
				FPS = false;
			}

		}
	}

	public void check()
	{

	}

	private void jump()
	{
		if (!isInAir)
		{
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	public void moveCamera()
	{
		float theta = yaw - 720;
		float offsetX = (float) (2 * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (2 * Math.cos(Math.toRadians(theta)));
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				position.z -= ((offsetZ * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x += ((offsetX * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			{
				position.z += ((offsetZ * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x -= ((offsetX * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else
			{
				position.x = position.x;
				position.z = position.z;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				position.z += ((offsetX * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x += ((offsetZ * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				position.z -= ((offsetX * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x -= ((offsetZ * RUN_SPEED * 3) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else
			{
				position.x = position.x;
				position.z = position.z;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			{
				jump();
			}
		}
		else
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_W))
			{

				position.z -= ((offsetZ * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x += ((offsetX * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			{

				position.z += ((offsetZ * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x -= ((offsetX * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else
			{
				position.x = position.x;
				position.z = position.z;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				position.z += ((offsetX * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x += ((offsetZ * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				position.z -= ((offsetX * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
				position.x -= ((offsetZ * RUN_SPEED) * DisplayManager.getCurrentFrameDurationSeconds());
			}
			else
			{
				position.x = position.x;
				position.z = position.z;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			{
				jump();
			}
		}
	}

	public boolean getFPS()
	{
		return FPS;
	}

	public Vector3f getPosition()
	{
		return position;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public float getPitch()
	{
		return pitch;
	}

	public float getYaw()
	{
		return yaw;
	}

	public float getRoll()
	{
		return roll;
	}

	private void calculateCameraPosition(float horizDistance,
			float verticDistance)
	{
		float theta = player.getRotation().y + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math
				.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math
				.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance;
	}

	private float calculateHorizontalDistance()
	{
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance()
	{
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private void calculateZoom()
	{
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}

	private void calculatePitch()
	{
		if (Mouse.isButtonDown(0))
		{
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}

	private void calculateAngleAroundPlayer()
	{
		if (Mouse.isButtonDown(0))
		{
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
