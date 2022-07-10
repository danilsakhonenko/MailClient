package pks.mailclient;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogMessage {
    String _message;
    Alert _alert;
    public DialogMessage(String message){
        _message = message;
    }
    
    public void ShowConsoleMessage(){
        System.out.println(_message);
    }
    
    public void InfoMessage(){
        _alert = new Alert(AlertType.INFORMATION);
        _alert.setTitle("Информация");
        ShowAlert();
    }
    
    public void WarningMessage(){
        _alert = new Alert(AlertType.WARNING);
        _alert.setTitle("Предупреждение");
        ShowAlert();
    }
    
    public void ErrorMessage(){
        _alert = new Alert(AlertType.ERROR);
        _alert.setTitle("Ошибка");
        ShowAlert();
    }
    
    public void ShowAlert(){
        if(_alert == null)
            return;
        _alert.setHeaderText(null);
        _alert.setContentText(_message);
        _alert.showAndWait();
    }
    
    
}
