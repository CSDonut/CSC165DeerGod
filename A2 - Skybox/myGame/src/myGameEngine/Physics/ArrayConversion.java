package myGameEngine.Physics;

public class ArrayConversion {
    public ArrayConversion() {
    }

    public float[] toFloatArray(double[] arr)
    { if (arr == null) return null;
        int n = arr.length;
        float[] ret = new float[n];
        for (int i = 0; i < n; i++)
        { ret[i] = (float)arr[i];
        }
        return ret;
    }
    public double[] toDoubleArray(float[] arr)
    { if (arr == null) return null;
        int n = arr.length;
        double[] ret = new double[n];
        for (int i = 0; i < n; i++)
        { ret[i] = (double)arr[i];
        }
        return ret;
    }
}

