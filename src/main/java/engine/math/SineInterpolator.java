package engine.math;

public class SineInterpolator {

    /**
     * Interpolates sine values across integer steps.
     *
     * @param start starting integer x value
     * @param end   ending integer x value
     * @return array of sine values at each integer step
     */
    public static double[] interpolateSine(int start, int end) {
        int length = end - start + 1;
        double[] values = new double[length];

        for (int i = 0; i < length; i++) {
            int x = start + i;
            values[i] = Math.sin(x); // x is in radians here
        }

        return values;
    }
}
