package com.example.panel;

/**
 * Created by zwb on 2017/8/27.
 */

public class GraphicsUtils {
    public static RectFloat RectFloatcalcLineFrame(float paramFloat1, float paramFloat2,
                                                   float paramFloat3, float paramFloat4) {
        RectFloat localRectFloat = new RectFloat();
        float f1 = (float)Math.sqrt(Math.pow(paramFloat3 - paramFloat1, 2.0D) + Math.pow(paramFloat4 - paramFloat2, 2.0D));
        float f2 = (40.0F + f1) / f1;
        float f3 = paramFloat3 + f2 * (paramFloat1 - paramFloat3);
        float f4 = paramFloat4 + f2 * (paramFloat2 - paramFloat4);
        float f5 = paramFloat1 + f2 * (paramFloat3 - paramFloat1);
        float f6 = paramFloat2 + f2 * (paramFloat4 - paramFloat2);
        float f7 = 40.0F * (-(paramFloat4 - paramFloat2) / f1);
        float f8 = 40.0F * ((paramFloat3 - paramFloat1) / f1);
        localRectFloat.x1 = (f3 + f7);
        localRectFloat.y1 = (f4 + f8);
        localRectFloat.x2 = (f3 - f7);
        localRectFloat.y2 = (f4 - f8);
        localRectFloat.x3 = (f5 + f7);
        localRectFloat.y3 = (f6 + f8);
        localRectFloat.x4 = (f5 - f7);
        localRectFloat.y4 = (f6 - f8);
        return localRectFloat;
    }

    public static RectFloat calcLineFrame(LineFloat paramLineFloat) {
        return RectFloatcalcLineFrame(paramLineFloat.x1, paramLineFloat.y1, paramLineFloat.x2, paramLineFloat.y2);
    }

    public static boolean testIntersect(float paramFloat1, float paramFloat2,
                                        float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
    {
        boolean bool = false;
        RectFloat localRectFloat = RectFloatcalcLineFrame(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
        if ((localRectFloat.x2 - localRectFloat.x1) * (paramFloat5 - localRectFloat.x1) + (localRectFloat.y2 - localRectFloat.y1) * (paramFloat6 - localRectFloat.y1) < 0.0F) {
            bool = false;
        }else {
            if (((localRectFloat.x4 - localRectFloat.x2) * (paramFloat5 - localRectFloat.x2)
                    + (localRectFloat.y4 - localRectFloat.y2) * (paramFloat6 - localRectFloat.y2) >= 0.0F)
                    && ((localRectFloat.x3 - localRectFloat.x4) * (paramFloat5 - localRectFloat.x4) +
                    (localRectFloat.y3 - localRectFloat.y4) * (paramFloat6 - localRectFloat.y4) >= 0.0F)
                    && ((localRectFloat.x1 - localRectFloat.x3) * (paramFloat5 - localRectFloat.x3) +
                    (localRectFloat.y1 - localRectFloat.y3) * (paramFloat6 - localRectFloat.y3) >= 0.0F)) {
                bool = true;
            }
        }

        return false;
    }

    public static class LineFloat
    {
        public float x1;
        public float x2;
        public float y1;
        public float y2;
    }

    public static class RectFloat
    {
        public float x1;
        public float x2;
        public float x3;
        public float x4;
        public float y1;
        public float y2;
        public float y3;
        public float y4;
    }
}
