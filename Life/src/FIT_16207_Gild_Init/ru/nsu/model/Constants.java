package FIT_16207_Gild_Init.ru.nsu.model;

public class Constants {
    public static float LIVE_BEGIN = (float) 2.0;
    public static float LIVE_END = (float) 3.3;
    public static float BIRTH_BEGIN = (float) 2.3;
    public static float BIRTH_END = (float) 2.9;
    public static float FST_IMPACT = (float) 1.0;
    public static float SND_IMPACT = (float) 0.3;

    public static void setLiveBegin(float liveBegin) throws Exception {
        if (liveBegin <= BIRTH_BEGIN && liveBegin >= 0) {
            LIVE_BEGIN = liveBegin;
           // return true;
        } else {
            LIVE_BEGIN = (float) 2.0;
            throw new Exception("Please set LIVE_BEGIN <= BIRTH_BEGIN");
        }
    }

    public static void setLiveEnd(float liveEnd) throws Exception {
        if (BIRTH_END <= liveEnd && liveEnd >= 0) {
            LIVE_END = liveEnd;
        } else {
            LIVE_END = (float) 3.3;
            throw new Exception("PLease set LIVE_END >= BIRTH_END");
        }
    }

    public static void setBirthBegin(float birthBegin) throws Exception {
        if (LIVE_BEGIN <= birthBegin && birthBegin <= BIRTH_END && birthBegin >= 0) {
            BIRTH_BEGIN = birthBegin;
        } else {
            BIRTH_BEGIN = (float) 2.3;
            throw new Exception("Please set BIRTH_BEGIN in between LIVE_BEGIN and BIRTH_END");
        }
    }

    public static void setBirthEnd(float birthEnd) throws Exception {
        if (BIRTH_BEGIN <= birthEnd && birthEnd <= LIVE_END && birthEnd >= 0) {
            BIRTH_END = birthEnd;
        } else {
            BIRTH_END = (float) 2.9;
            throw new Exception("Please set BIRTH_END in between BIRTH_BEGIN and LIVE_END");
        }
    }

    public static void setFstImpact(float fstImpact) throws Exception {
        if (fstImpact >= 0) {
            FST_IMPACT = fstImpact;
        } else {
            FST_IMPACT = (float) 1.0;
            throw new Exception("FIRST_IMPACT must be non-negative");
        }
    }

    public static void setSndImpact(float sndImpact) throws Exception {
        if (sndImpact >= 0) {
            SND_IMPACT = sndImpact;
        } else {
            SND_IMPACT = (float) 0.3;
            throw new Exception("SECOND_IMPACT must be non-negative");
        }
    }
}
