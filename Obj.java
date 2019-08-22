package rlbotexample;

import rlbotexample.vector.Vector3;

public class Obj {
    public Vector3 location = new Vector3(0,0,0);
    public Vector3 velocity = new Vector3(0,0,0);
    public Vector3 rotation = new Vector3(0,0,0);
    public Vector3 rvelocity = new Vector3(0,0,0);

    public Vector3 local_location = new Vector3(0,0,0);
    public Vector3 matrix[] = new Vector3[3];
    public Obj()
    {
        for(int i = 0; i < matrix.length; i++)
        {
            matrix[i] = new Vector3(0,0,0);
        }
    }


}
