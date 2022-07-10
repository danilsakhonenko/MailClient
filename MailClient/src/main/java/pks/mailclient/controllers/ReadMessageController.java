package pks.mailclient.controllers;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pks.mailclient.DialogMessage;
import pks.mailclient.sessions.MailReadSession;

public class ReadMessageController {
    MailReadSession _session;

    @FXML
    private TextField email_field;

    @FXML
    private Button loadFiles_btn;

    @FXML
    private TextArea message_field;

    @FXML
    private TextField subject_field;

    @FXML
    void loadFiles(ActionEvent event) {
        try {
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Выбор каталога для сохранения");
            File selectedDir = dirChooser.showDialog(new Stage());
            if (selectedDir == null) {
                return;
            }
            _session.loadFiles(selectedDir);
            new DialogMessage("Файлы загружены по указанному пути").InfoMessage();
        } catch (IOException ex) {
            new DialogMessage("При загрузке файлов возникла ошибка").ErrorMessage();
        } catch (Exception ex1) {
            new DialogMessage("Сообщение не создержит прикрепленных файлов").WarningMessage();
        }
    }

    public void setMessage(MailReadSession session, int msgIndex) throws Exception {
        _session = session;
        String[] items = _session.getMessage(msgIndex);
        email_field.setText(items[0]);
        subject_field.setText(items[1]);
        message_field.setText(items[2]);
    }

    @FXML
    public void initialize() {
    }

}
