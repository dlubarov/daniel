package daniel.nagger.messages.s2c;

public class S2cMessage {
  public S2cCreateAlertMessage createAlert;
  public S2cAddCheckMessage addCheck;
  public S2cAddTagMessage addTag;
  public S2cEditAlertNameMessage editAlertName;
  public S2cEditAlertDescriptionMessage editAlertDescription;
  public S2cEditAlertCommandMessage editAlertCommand;
  public S2cJumpToAlertMessage jumpToAlert;
  public S2cEditRecipientCommandMessage editRecipientCommand;
}
