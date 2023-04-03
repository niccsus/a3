package tage.shapes;
import tage.*;
import org.joml.*;
import static java.lang.Math.*;
public class Star extends ManualObject
{	//manual object the pyramid ontop of itself, why is it's dementions off
    private float[] vertices = new float[]
            { -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                    1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                    1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                    -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                    -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f,

                    -1.0f, 2.0f, 1.0f, 1.0f, 2.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                    1.0f, 2.0f, 1.0f, 1.0f, 2.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                    1.0f, 2.0f, -1.0f, -1.0f, 2.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                    -1.0f, 2.0f, -1.0f, -1.0f, 2.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                    -1.0f, 2.0f, -1.0f, 1.0f, 2.0f, 1.0f, -1.0f, 2.0f, 1.0f,
                    1.0f, 2.0f, 1.0f, -1.0f, 2.0f, -1.0f, 1.0f, 2.0f, -1.0f, };
    private float[] texcoords = new float[]
            { 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                    0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
    private float[] normals = new float[]
            { 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,

                    0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f };
    public Star()
    { 	super();
        setNumVertices(36);
        setVertices(vertices);
        setTexCoords(texcoords);
        setNormals(normals);
        setMatAmb(Utils.goldAmbient());
        setMatDif(Utils.goldDiffuse());
        setMatSpe(Utils.goldSpecular());
        setMatShi(Utils.goldShininess());
    }
}
/*============================================================================*/