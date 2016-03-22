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

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;
import org.catrobat.catroid.formulaeditor.Formula;

import java.util.List;

public class SayBrick extends BubbleBrick {

	private static final long serialVersionUID = 1L;

	public SayBrick() {
		addAllowedBrickField(BrickField.BUBBLE_TEXT);
		addAllowedBrickField(BrickField.BUBBLE_DURATION);
	}

	public SayBrick(String say) {
		initializeBrickFields(new Formula(say), new Formula((Integer.MAX_VALUE)));
	}

	public SayBrick(Formula say) {
		initializeBrickFields(say, new Formula((Integer.MAX_VALUE)));
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		view = View.inflate(context, R.layout.brick_say, null);
		view = getViewWithAlpha(alphaValue);
		setCheckboxView(R.id.brick_say_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		view.findViewById(R.id.brick_say_prototype_text_view).setVisibility(View.GONE);
		TextView sayEditText = (TextView) view.findViewById(R.id.brick_say_edit_text);
		sayEditText.setVisibility(View.VISIBLE);
		sayEditText.setOnClickListener(this);
		getFormulaWithBrickField(BrickField.BUBBLE_TEXT).setTextFieldId(R.id.brick_say_edit_text);
		getFormulaWithBrickField(BrickField.BUBBLE_TEXT).refreshTextField(view);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, R.layout.brick_say, null);
		TextView sayPrototypeTextView = (TextView) prototypeView.findViewById(R.id.brick_say_prototype_text_view);
		sayPrototypeTextView.setText(BrickValues.SAY);
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {
		if (view != null) {
			View layout = view.findViewById(R.id.brick_say_layout);
			layout.getBackground().setAlpha(alphaValue);

			TextView sayTextView = (TextView) view.findViewById(R.id.brick_say_textview);
			sayTextView.setTextColor(sayTextView.getTextColors().withAlpha(alphaValue));
			TextView sayEditText = (TextView) view.findViewById(R.id.brick_say_edit_text);
			sayEditText.setTextColor(sayEditText.getTextColors().withAlpha(alphaValue));
			sayEditText.getBackground().setAlpha(alphaValue);

			this.alphaValue = (alphaValue);
		}
		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		bubbleLeftView.setBackgroundResource(R.drawable.bubble_say_right);
		((TextView) bubbleLeftView.findViewById(R.id.bubble_edit_text)).setText(getNormalizedText(sprite));
		((TextView) bubbleLeftView.findViewById(R.id.bubble_edit_text)).setTextColor(Color.BLACK);
		rightBubble = bubbleWithTextFromDrawingCache(bubbleLeftView);

		bubbleRightView.setBackgroundResource(R.drawable.bubble_say_left);
		((TextView) bubbleRightView.findViewById(R.id.bubble_edit_text)).setText(getNormalizedText(sprite));
		((TextView) bubbleRightView.findViewById(R.id.bubble_edit_text)).setTextColor(Color.BLACK);
		leftBubble = bubbleWithTextFromDrawingCache(bubbleRightView);

		sequence.addAction(ExtendedActions.say(sprite, rightBubble, leftBubble,
				getFormulaWithBrickField(BrickField.BUBBLE_DURATION)));
		return null;
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
	}

}
