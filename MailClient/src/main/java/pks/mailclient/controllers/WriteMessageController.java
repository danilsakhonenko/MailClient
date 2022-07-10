package pks.mailclient.controllers;

import java.io.File;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pks.mailclient.DialogMessage;
import pks.mailclient.sessions.SmtpSession;

public class WriteMessageController {
    private SmtpSession _smtp;
    private List<File> _list;
    
    @FXML
    private Button addFile_btn;

    @FXML
    private TextField email_field;

    @FXML
    private Label fileCount_label;

    @FXML
    private TextArea message_field;

    @FXML
    private Button return_btn;

    @FXML
    private Button send_btn;

    @FXML
    private TextField subject_field;
    
    @FXML
    void addFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор файла");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files(*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        _list = fileChooser.showOpenMultipleDialog(new Stage());
        if (_list == null || _list.size() == 0) 
            return;
        Integer count = Integer.parseInt(fileCount_label.getText())+_list.size();
        fileCount_label.setText(count.toString());
    }

    @FXML
    void sendLetter(ActionEvent event) {
        try {
            _smtp.sendMail(email_field.getText(), subject_field.getText(), message_field.getText(),_list);
            new DialogMessage("Сообщение отправлено").InfoMessage();
        } catch (Exception ex) {
            new DialogMessage("При отправке сообщения возникла оишбка").ErrorMessage();
        }
    }
    
    public void setSession(SmtpSession smtp){
        _smtp = smtp;
    }

    @FXML
    void goToStart(ActionEvent event) {
        Stage stage = (Stage)email_field.getScene().getWindow();
        stage.close();
        StartController.unHide();
    }
    
    @FXML
    void initialize() {
        fileCount_label.setText("0");
    }

}