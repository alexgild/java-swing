package FIT_16207_Gild_Init.ru.nsu.model;

public class CoordinateConvert {
    public static float pixelToCoordinate(int pixelParameter, int definitionStart, int definitionEnd, int leftFrameCorner, int rightFrameCorner) {
        return ((definitionEnd - definitionStart) * (float)(pixelParameter - leftFrameCorner)/(float)(rightFrameCorner - leftFrameCorner) + definitionStart);
    }
}
