/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad3;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 *
 * @author rajar
 */
public class undoableEditHappened implements UndoableEditListener {

    UndoManager um = new UndoManager();

    public void undoableEditHappened(UndoableEditEvent e) {
        //Remember the edit and update the menus
        um.addEdit(e.getEdit());
    }

}
