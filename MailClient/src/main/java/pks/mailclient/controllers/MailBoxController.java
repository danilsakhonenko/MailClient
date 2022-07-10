package pks.mailclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import pks.mailclient.DialogMessage;
import pks.mailclient.sessions.MailReadSession;

public class MailBoxController {

    private MailReadSession _session;
    private int _currMessage;

    @FXML
    private ListView<String> letters_list;

    @FXML
    private Button reload_btn;

    @FXML
    private Button return_btn;

    @FXML
    void reloadInbox(ActionEvent event) {
        try {
            if (!_session.getTransfer()) {
                reload_btn.setText("Остановить");
                _session.setTransfer(true);
                _session.getAllMessages(letters_list);
            }else{
                _session.setTransfer(false);
                reload_btn.setText("Обновить");
            }
        } catch (Exception ex) {
            new DialogMessage("При загрузке сообщений возникла ошибка").ErrorMessage();
        }
    }

    @FXML
    void openMessage(ActionEvent event) {

        Stage newstage = null;
        try {
            String s = letters_list.getItems().get(letters_list.getSelectionModel().getSelectedIndex());
            _currMessage = Integer.parseInt(s.substring(0, s.indexOf(" ")));
            if(_session.getTransfer())
                reloadInbox(new ActionEvent());
            newstage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReadMessageForm.fxml"));
            newstage.setScene(new Scene(loader.load()));
            ReadMessageController ctrl = loader.getController();
            newstage.initModality(Modality.APPLICATION_MODAL);
            newstage.setResizable(false);
            newstage.show();
            ctrl.setMessage(_session, _currMessage);
        } catch (Exception ex) {
            if(newstage !=null)
                newstage.close();
            new DialogMessage("При открытии сообщения возникла ошибка").ErrorMessage();
        }
    }

    @FXML
    void goToStart(ActionEvent event) throws MessagingException{
        _session.closeConnection(); 
        Stage stage = (Stage) return_btn.getScene().getWindow();
        stage.close();
        StartController.unHide();
    }

    public void setSession(MailReadSession session) throws MessagingException {
        _session = session;
        _session.openConnection();
    }

    @FXML
    void initialize() {
    }
}
