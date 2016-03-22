/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2015 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class BubbleBrick extends FormulaBrick implements OnClickListener, Cloneable {

	protected static final int STRING_OFFSET = 20;
	protected static final int BOUNDARY_PIXEL = 30;
	protected static final StringBuilder STRING_BUILDER = new StringBuilder();

	protected transient View prototypeView;
	protected transient View bubbleLeftView;
	protected transient View bubbleRightView;
	protected transient byte[] rightBubble;
	protected transient byte[] leftBubble;


	protected void initializeBrickFields(Formula think, Formula duration) {
		addAllowedBrickField(BrickField.BUBBLE_TEXT);
		addAllowedBrickField(BrickField.BUBBLE_DURATION);
		setFormulaWithBrickField(BrickField.BUBBLE_TEXT, think);
		setFormulaWithBrickField(BrickField.BUBBLE_DURATION, duration);
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		switch (view.getId()) {
			case R.id.brick_say_edit_text:
				FormulaEditorFragment.showFragment(view, this, BrickField.BUBBLE_TEXT);
				break;

			case R.id.brick_think_edit_text:
				FormulaEditorFragment.showFragment(view, this, BrickField.BUBBLE_TEXT);
				break;

			case R.id.brick_for_edit_text:
				FormulaEditorFragment.showFragment(view, this, BrickField.BUBBLE_DURATION);
				break;
		}
	}

	protected String getNormalizedText(Sprite sprite) {
		Object interpretation = getFormulaWithBrickField(BrickField.BUBBLE_TEXT).interpretObject(sprite);
		String text = "Error";

		if (interpretation instanceof String){
			text = (String) interpretation;
		}else if (interpretation instanceof Double){
			text = String.valueOf(interpretation);
		}

		STRING_BUILDER.delete(0, STRING_BUILDER.length());

		for (int index = 0; index < text.length(); index++) {
			if (index % STRING_OFFSET == 0) {
				STRING_BUILDER.append('\n');
			}
			//TODO: max size of text.
			STRING_BUILDER.append(text.charAt(index));
		}
		return STRING_BUILDER.toString();
	}

	protected byte[] bubbleWithTextFromDrawingCache(View bubble) {
		bubble.setDrawingCacheEnabled(true);
		bubble.measure(MeasureSpec.makeMeasureSpec(ScreenValues.SCREEN_WIDTH - BOUNDARY_PIXEL, MeasureSpec.AT_MOST),
				MeasureSpec.makeMeasureSpec(ScreenValues.SCREEN_HEIGHT - BOUNDARY_PIXEL, MeasureSpec.AT_MOST));
		bubble.layout(0, 0, bubble.getMeasuredWidth(), bubble.getMeasuredHeight());

		Bitmap bitmap = bubble.getDrawingCache();
		ByteArrayOutputStream stream = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
		byte[] bubbleWithText = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException iOException) {
			Log.d(BubbleBrick.class.getSimpleName(), "Can not close bubbles byte array stream!", iOException);
		}

		bubble.setDrawingCacheEnabled(false);
		bitmap.recycle();
		bitmap = null;
		return bubbleWithText;
	}

	public void setBubbleLayout(View bubbleLeft, View bubbleRight) {
		this.bubbleLeftView = bubbleLeft;
		this.bubbleRightView = bubbleRight;
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.BUBBLE_TEXT);
	}

}
