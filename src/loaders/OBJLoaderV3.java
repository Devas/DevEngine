package loaders;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class OBJLoaderV3 {

    private final Loader loader;
    private static int verticesNumber;
    private static int texturesNumber;
    private static int normalsNumber;
    private static int facesNumber;

    public OBJLoaderV3(Loader loader) {
        this.loader = loader;
    }

    public RawModel loadObjModel(String fileName) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File("res/" + fileName + ".obj"));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fileReader);

        List<Vector3f> verticesList = new ArrayList<>();
        List<Vector2f> texturesList = new ArrayList<>();
        List<Vector3f> normalsList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        try {
            String line;
            while (true) {
                line = reader.readLine();
                String[] lineSplit = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2]),
                            Float.parseFloat(lineSplit[3]));
                    verticesList.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2]));
                    texturesList.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2]),
                            Float.parseFloat(lineSplit[3]));
                    normalsList.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }

            verticesNumber = verticesList.size();
            texturesNumber = texturesList.size();
            normalsNumber = normalsList.size();
            texturesArray = new float[verticesNumber * 2]; // 2-value uv-vector per vertex
            normalsArray = new float[verticesNumber * 3];  // 3-value normal-vector per vertex

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                facesNumber++;
                String[] triangleData = line.split(" ");
                String[] vertexData1 = triangleData[1].split("/");
                String[] vertexData2 = triangleData[2].split("/");
                String[] vertexData3 = triangleData[3].split("/");
                processVertex(vertexData1, indicesList, texturesList, normalsList, texturesArray, normalsArray);
                processVertex(vertexData2, indicesList, texturesList, normalsList, texturesArray, normalsArray);
                processVertex(vertexData3, indicesList, texturesList, normalsList, texturesArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[verticesNumber * 3]; // 3-value position-vector per vertex
        System.out.println(indicesList.size());
        indicesArray = new int[indicesList.size()];
        int vertexPointer = 0;
        for (Vector3f vertex : verticesList) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }
        for (int i = 0; i < indicesList.size(); i++) {
            indicesArray[i] = indicesList.get(i);
        }
        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    private void processVertex(String[] vertexData, List<Integer> indicesList,
                                      List<Vector2f> texturesList, List<Vector3f> normalsList,
                                      float[] texturesArray, float[] normalsArray) {

        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1; // -1 because .obj enumerates from 1
        indicesList.add(currentVertexPointer);

        int currentTexturePointer = Integer.parseInt(vertexData[1]) - 1;
        Vector2f currentTexture = texturesList.get(currentTexturePointer);
        texturesArray[currentVertexPointer * 2] = currentTexture.x;
        // OpenGL UV coordinates have the origin in the top-left corner, but Blender / 3dsMax in the bottom left corner
        texturesArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y; // so we subtract
//        try {
//            Vector2f currentTexture = texturesList.get(Integer.parseInt(vertexData[1]) - 1);
//            textureArray[currentVertexPointer * 2] = currentTexture.x;
//            textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;
//        } catch (Exception e) {
//            Vector2f currentTexture = new Vector2f(0, 0);
//            textureArray[currentVertexPointer * 2] = currentTexture.x;
//            textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;
//        }

        int currentNormalPointer = Integer.parseInt(vertexData[2]) - 1;
        Vector3f currentNormal = normalsList.get(currentNormalPointer);
        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }

    public void printLastLoadInfo() {
        System.out.println("Vertices: " + verticesNumber);
        System.out.println("UV coordinates: " + texturesNumber);
        System.out.println("Normals: " + normalsNumber);
        System.out.println("Faces: " + facesNumber);
    }

}
