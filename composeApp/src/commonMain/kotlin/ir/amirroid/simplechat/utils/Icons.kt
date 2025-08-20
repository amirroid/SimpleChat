/*
 * Copyright 2024 The Android Open Source Project
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
 */

package ir.amirroid.simplechat.utils

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val DoneAll: ImageVector
    get() {
        if (_doneAll != null) {
            return _doneAll!!
        }
        _doneAll = materialIcon(name = "Rounded.DoneAll") {
            materialPath {
                moveTo(17.3f, 6.3f)
                curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                lineToRelative(-5.64f, 5.64f)
                lineToRelative(1.41f, 1.41f)
                lineTo(17.3f, 7.7f)
                curveToRelative(0.38f, -0.38f, 0.38f, -1.02f, 0.0f, -1.4f)
                close()
                moveTo(21.54f, 6.29f)
                lineToRelative(-9.88f, 9.88f)
                lineToRelative(-3.48f, -3.47f)
                curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                curveToRelative(-0.39f, 0.39f, -0.39f, 1.02f, 0.0f, 1.41f)
                lineToRelative(4.18f, 4.18f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0.0f)
                lineTo(22.95f, 7.71f)
                curveToRelative(0.39f, -0.39f, 0.39f, -1.02f, 0.0f, -1.41f)
                horizontalLineToRelative(-0.01f)
                curveToRelative(-0.38f, -0.4f, -1.01f, -0.4f, -1.4f, -0.01f)
                close()
                moveTo(1.12f, 14.12f)
                lineTo(5.3f, 18.3f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0.0f)
                lineToRelative(0.7f, -0.7f)
                lineToRelative(-4.88f, -4.9f)
                curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                curveToRelative(-0.39f, 0.39f, -0.39f, 1.03f, 0.0f, 1.42f)
                close()
            }
        }
        return _doneAll!!
    }

private var _doneAll: ImageVector? = null


val Check: ImageVector
    get() {
        if (_check != null) {
            return _check!!
        }
        _check = materialIcon(name = "Rounded.Check") {
            materialPath {
                moveTo(9.0f, 16.17f)
                lineTo(5.53f, 12.7f)
                curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                curveToRelative(-0.39f, 0.39f, -0.39f, 1.02f, 0.0f, 1.41f)
                lineToRelative(4.18f, 4.18f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0.0f)
                lineTo(20.29f, 7.71f)
                curveToRelative(0.39f, -0.39f, 0.39f, -1.02f, 0.0f, -1.41f)
                curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                lineTo(9.0f, 16.17f)
                close()
            }
        }
        return _check!!
    }

private var _check: ImageVector? = null

val Send: ImageVector
    get() {
        if (_send != null) {
            return _send!!
        }
        _send = materialIcon(name = "Rounded.Send") {
            materialPath {
                moveTo(3.4f, 20.4f)
                lineToRelative(17.45f, -7.48f)
                curveToRelative(0.81f, -0.35f, 0.81f, -1.49f, 0.0f, -1.84f)
                lineTo(3.4f, 3.6f)
                curveToRelative(-0.66f, -0.29f, -1.39f, 0.2f, -1.39f, 0.91f)
                lineTo(2.0f, 9.12f)
                curveToRelative(0.0f, 0.5f, 0.37f, 0.93f, 0.87f, 0.99f)
                lineTo(17.0f, 12.0f)
                lineTo(2.87f, 13.88f)
                curveToRelative(-0.5f, 0.07f, -0.87f, 0.5f, -0.87f, 1.0f)
                lineToRelative(0.01f, 4.61f)
                curveToRelative(0.0f, 0.71f, 0.73f, 1.2f, 1.39f, 0.91f)
                close()
            }
        }
        return _send!!
    }

private var _send: ImageVector? = null