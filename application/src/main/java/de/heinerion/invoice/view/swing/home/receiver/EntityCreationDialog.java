package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import de.heinerion.invoice.view.swing.home.receiver.forms.AbstractForm;
import de.heinerion.invoice.view.swing.menu.BusyFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.Consumer;

@Flogger
public abstract class EntityCreationDialog<T> {
  private final Consumer<T> callback;
  private final Session session;
  private BusyFrame originFrame;
  /**
   * window adapter, responsible for closing
   */
  private final DisposeAdapter closer = new DisposeAdapter();
  private JDialog dialog;

  private JButton btnSave;
  private JButton btnCancel;
  private JPanel formPanel;
  private AbstractForm<T> form;
  private JComboBox<EntityWrapper<T>> entityDropDown;

  private final JpaRepository<T, ?> entityRepository;

  protected EntityCreationDialog(Session session, JpaRepository<T, ?> entityRepository, Consumer<T> callback) {
    this.entityRepository = entityRepository;
    this.callback = callback;
    this.session = session;
  }

  /**
   * shows an always on top modal menu and sets the origin frame busy.
   */
  public void showDialog() {
    ApplicationFrame applicationFrame = session.getApplicationFrame();
    originFrame = new BusyFrame(applicationFrame.getFrame());
    originFrame.setBusy(true);

    dialog = new JDialog(originFrame.frame(), true);
    showDialog(dialog);
  }

  private void showDialog(JDialog modalDialog) {
    modalDialog.setAlwaysOnTop(true);
    modalDialog.addWindowListener(closer);
    createWidgets();
    addWidgets(modalDialog);
    setupInteractions(modalDialog);
    modalDialog.setTitle(getDialogTitle());
    modalDialog.pack();

    modalDialog.setLocationRelativeTo(originFrame.frame());
    modalDialog.setVisible(true);
  }

  protected abstract String getDialogTitle();

  private void createWidgets() {
    form = createForm();
    formPanel = form.getPanel();
    entityDropDown = createCompanyBox();

    btnSave = new JButton(Translator.translate("controls.save"));
    btnCancel = new JButton(Translator.translate("controls.cancel"));
  }

  private JComboBox<EntityWrapper<T>> createCompanyBox() {
    Vector<EntityWrapper<T>> options = new Vector<>(collectValues());
    options.add(0, new EntityWrapper<>(null));
    JComboBox<EntityWrapper<T>> companyBox = new JComboBox<>(
        options
    );
    companyBox.setOpaque(false);
    companyBox.addActionListener(e -> toggleEditableFields());

    return companyBox;
  }

  private Collection<EntityWrapper<T>> collectValues() {
    return entityRepository
        .findAll()
        .stream()
        .sorted()
        .map(EntityWrapper<T>::new)
        .toList();
  }

  protected void toggleEditableFields() {
    form.setEditable(isNullValueSelected());
  }

  private boolean isNullValueSelected() {
    return entityDropDown.getSelectedIndex() == 0;
  }

  @RequiredArgsConstructor
  private static class EntityWrapper<T> {
    private final T entity;

    public T getEntity() {
      return entity;
    }

    @Override
    public String toString() {
      return entity == null
          ? "-"
          : String.valueOf(entity);
    }
  }

  protected abstract AbstractForm<T> createForm();

  private void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout(5, 5));

    dialog.add(entityDropDown, BorderLayout.PAGE_START);

    dialog.add(formPanel, BorderLayout.CENTER);

    JPanel buttons = new JPanel(new BorderLayout());
    buttons.add(btnCancel, BorderLayout.LINE_START);
    buttons.add(btnSave, BorderLayout.LINE_END);
    dialog.add(buttons, BorderLayout.PAGE_END);
  }

  private void setupInteractions(JDialog dialog) {
    btnSave.addActionListener(e -> saveEntity(dialog));
    btnCancel.addActionListener(e -> closeDialog(dialog));
  }

  private void saveEntity(JDialog dialog) {
    T entity = isNullValueSelected()
        ? persistFormEntity()
        : getEntityFromDropDown();

    closeDialog(dialog);

    if (entity != null) {
      log.atFine().log("chose %s", entity);
      callback.accept(entity);
    }
  }

  private T persistFormEntity() {
    T entity = getEntityFromForm();
    if (entity == null) {
      return null;
    }
    log.atFine().log("save entity %s", entity);
    return entityRepository.save(entity);
  }

  private T getEntityFromDropDown() {
    return entityDropDown
        .getItemAt(entityDropDown.getSelectedIndex())
        .getEntity();
  }

  protected T getEntityFromForm() {
    return form.getValue();
  }

  private void closeDialog(JDialog dialog) {
    dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
  }

  protected final class DisposeAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      dialog.dispose();
      originFrame.setBusy(false);
    }
  }

}
