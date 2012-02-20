/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * file was slightly modified by me 
 */

package com.samsung.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class AnimDrawable extends ProxyDrawable {
    
	private int oldWidth;
	private int oldHeight;
	private Core handle;
    
    public AnimDrawable(Drawable target, Bitmap bm) {
        super(target);

		oldWidth = bm.getWidth();
		oldHeight = bm.getHeight();
        handle = new Core(bm);
    }

    @Override
    public void draw(Canvas canvas) {
    	handle.newframe();                
    	int[] out = handle.getOut();
    	Paint paint = new Paint();
    	canvas.drawBitmap(out, 0, oldWidth, 0, 0, 
    			oldWidth, oldHeight, false, paint);
    }

	public void click(float x, float y) {
		handle.disturb((int)x, (int)y);
	}
}