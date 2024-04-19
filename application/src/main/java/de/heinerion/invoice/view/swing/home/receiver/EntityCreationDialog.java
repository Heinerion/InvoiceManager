package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.view.swing.home.ApplicationFrame;
import de.heinerion.invoice.view.swing.home.receiver.forms.AbstractForm;
import de.heinerion.invoice.view.swing.menu.BusyFrame;
import lombok.*;
import lombok.extern.flogger.Flogger;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.Consumer;

@Flogger
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class EntityCreationDialog<T> {
  private final Session session;
  private final JpaRepository<T, ?> entityRepository;
  private final Consumer<T> callback;

  /**
   * window adapter, responsible for closing
   */
  private final DisposeAdapter closer = new DisposeAdapter();

  private BusyFrame originFrame;
  private JDialog dialog;
  private JButton btnSave;
  private JButton btnCancel;
  private JPanel formPanel;
  private AbstractForm<T> form;
  private JComboBox<EntityWrapper<T>> entityDropDown;

  /**
   * shows an always on top modal menu and sets the origin frame busy.
   */
  public void showDialog() {
    createBusyFrame();
    createDialog();
  }

  private void createBusyFrame() {
    ApplicationFrame applicationFrame = session.getApplicationFrame();
    originFrame = new BusyFrame(applicationFrame.getFrame());
    originFrame.setBusy(true);
  }

  private void createDialog() {
    dialog = new JDialog(originFrame.frame(), true);
    dialog.setAlwaysOnTop(true);
    dialog.addWindowListener(closer);
    createWidgets();
    addWidgets(dialog);
    setupInteractions(dialog);
    dialog.setTitle(getDialogTitle());
    dialog.pack();

    dialog.setLocationRelativeTo(originFrame.frame());
    dialog.setVisible(true);
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

  private record EntityWrapper<T>(T entity) {
    @Override
    public String toString() {
      return Optional.ofNullable(entity)
          .map(String::valueOf)
          .orElse("-");
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
    Optional<T> entity = getEntityFromDropDown()
        .or(this::persistFormEntity);

    closeDialog(dialog);

    log.atFine().log("chose %s", entity.map(String::valueOf).orElse("nothing"));
    entity.ifPresent(callback);
  }

  private Optional<T> persistFormEntity() {
    return getEntityFromForm()
        .map(this::save);
  }

  private T save(T entity) {
    log.atFine().log("save entity %s", entity);
    return entityRepository.save(entity);
  }

  private Optional<T> getEntityFromDropDown() {
    if (isNullValueSelected()) {
      return Optional.empty();
    }

    return Optional.ofNullable(entityDropDown
        .getItemAt(entityDropDown.getSelectedIndex())
        .entity());
  }

  protected Optional<T> getEntityFromForm() {
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
