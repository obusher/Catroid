/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
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
package org.catrobat.catroid.test.content.actions;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;
import org.catrobat.catroid.content.actions.GlideToAction;

import android.test.AndroidTestCase;

public class GlideToActionTest extends AndroidTestCase {

	int xPosition = 100;
	int yPosition = 100;
	int duration = 1000;

	public void testNormalBehavior() throws InterruptedException {
		Sprite sprite = new Sprite("testSprite");
		assertEquals("Unexpected initial sprite x position", 0f, sprite.look.getXPosition());
		assertEquals("Unexpected initial sprite y position", 0f, sprite.look.getYPosition());
		sprite.look.setWidth(100.0f);
		sprite.look.setHeight(50.0f);

		GlideToAction action = ExtendedActions.glideTo(xPosition, yPosition, duration);
		sprite.look.addAction(action);
		long currentTimeDelta = System.currentTimeMillis();
		do {
			currentTimeDelta = System.currentTimeMillis() - currentTimeDelta;
		} while (!action.act(currentTimeDelta));

		assertEquals("Incorrect sprite x position after GlideToBrick executed", (float) xPosition,
				sprite.look.getXPosition());
		assertEquals("Incorrect sprite y position after GlideToBrick executed", (float) yPosition,
				sprite.look.getYPosition());
	}

	public void testNullActor() {
		GlideToAction action = ExtendedActions.glideTo(xPosition, yPosition, duration);
		try {
			action.act(1.0f);
			fail("Execution of GlideToBrick with null Sprite did not cause a " + "NullPointerException to be thrown");
		} catch (NullPointerException expected) {
			// expected behavior
		}
	}

	public void testBoundaryPositions() {
		Sprite sprite = new Sprite("testSprite");

		GlideToAction action = ExtendedActions.placeAt(Integer.MAX_VALUE, Integer.MAX_VALUE);
		sprite.look.addAction(action);
		action.act(1.0f);

		assertEquals("PlaceAtBrick failed to place Sprite at maximum x float value", (float) Integer.MAX_VALUE,
				sprite.look.getXPosition());
		assertEquals("PlaceAtBrick failed to place Sprite at maximum y float value", (float) Integer.MAX_VALUE,
				sprite.look.getYPosition());

		action = ExtendedActions.placeAt(Integer.MIN_VALUE, Integer.MIN_VALUE);
		sprite.look.addAction(action);
		action.act(1.0f);

		assertEquals("PlaceAtBrick failed to place Sprite at minimum x float value", (float) Integer.MIN_VALUE,
				sprite.look.getXPosition());
		assertEquals("PlaceAtBrick failed to place Sprite at minimum y float value", (float) Integer.MIN_VALUE,
				sprite.look.getYPosition());
	}
}