# Verteilte Systeme: MessageListener

## Setup für das Projekt
Damit das Projekt funktioniert, muss noch ActiveMQ als Message Broker aufgesetzt werden. <br>
Anstatt zu installieren kann man das auch einfach über Docker laufen lassen.
### Docker aufsetzten
1. Docker Desktop starten
2. In einer Command Shell den Befehl `docker run -p 61616:61616 -p 8161:8161 --name activemq -d rmohr/activemq`ausführen.
Im Browser kann das ActiveMQ Interface jetzt unter [localhost:8161/admin/](http://localhost:8161/admin/) aufgerufen werden. <br>

Die Standard Login-Daten sind:
<table>
  <tr>
    <td>Benutzername:</td>
    <td>admin</td>
  </tr>
  <tr>
    <td>Passwort:</td>
    <td>admin</td>
  </tr>
</table>
Nötig ist das aber nicht.

### Projekt verwenden
1. Die beiden Receiver starten: [Queue Reciver](MessageListenerArtifactID/src/main/java/messageListener/MessageListenerQueueReceiver.java), [Topic Receiver](MessageListenerArtifactID/src/main/java/messageListener/MessageListenerTopicReceiver.java)
2. Den Sender starten: [Sender](MessageListenerArtifactID/src/main/java/messageListener/MessageListenerSender.java)

Jedes mal, wenn der Sender gestartet wird, empfangen die beiden Receiver die Nachricht, die gesendet wurde.
