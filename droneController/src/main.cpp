#include <Arduino.h>
#include "WiFi.h"
#include "AsyncUDP.h"

const char *ssid = "Mit Mobile Hotspot. Stay away";
const char *password = "x½";

enum Movement
{
  left,
  right,
  forward,
  backward,
  stay,
  up,
  down,
  land,
  takeOff,
  cw,
  ccw

};
bool inAir = false;

const int switchPin = 17;
const int potentiometer = 36;
const int yPin = 39;
const int xPin = 34;
const int ccwButton = 14;
const int cwButton = 15;

String speed = "21";

AsyncUDP udp;

void setup()
{

  pinMode(switchPin, INPUT_PULLDOWN);
  pinMode(potentiometer, INPUT);
  pinMode(cwButton, INPUT_PULLUP);
  pinMode(ccwButton, INPUT_PULLUP);
  pinMode(xPin, INPUT);
  pinMode(yPin, INPUT);
  Serial.begin(9600);

  

 WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  if (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("WiFi Failed");
    while (1) {
      delay(1000);
    }
  }
  if (udp.listen(4000)) {
    Serial.print("UDP Listening on IP: ");
    Serial.println(WiFi.localIP());
    udp.onPacket([](AsyncUDPPacket packet) {
      Serial.print("UDP Packet Type: ");
      Serial.print(packet.isBroadcast()
                       ? "Broadcast"
                       : packet.isMulticast() ? "Multicast" : "Unicast");
      Serial.print(", From: ");
      Serial.print(packet.remoteIP());
      Serial.print(":");
      Serial.print(packet.remotePort());
      Serial.print(", To: ");
      Serial.print(packet.localIP());
      Serial.print(":");
      Serial.print(packet.localPort());
      Serial.print(", Length: ");
      Serial.print(packet.length());
      Serial.print(", Data: ");
      Serial.write(packet.data(), packet.length());
      Serial.println();

      // reply to the client/sender
      packet.printf("Got %u bytes of data", packet.length());
    }); 
  }

}


void sendMessage(String msg){
  udp.writeTo((const uint8_t *)msg.c_str(), msg.length(),
              IPAddress(192, 168, 43, 57), 4001);
Serial.println(msg);

}




bool isButtonPressed(int buttonState)
{
  if (buttonState == 0)
  {
    return true;
  }
  else
  {
    return false;
  }
}

void doStuff(Movement movement)
{
  switch (movement)
  {
  case left:
    sendMessage("left");
    break;
  case right:
    sendMessage("right");
    break;
  case forward:
    sendMessage("forward");
    break;
  case backward:
    sendMessage("back");
    break;
  case land:
    sendMessage("land");
    inAir = false;
    break;
  case takeOff:
    sendMessage("takeOff");
    inAir = true;
    break;
  case cw:
    sendMessage("cw");
    break;
  case ccw:
    sendMessage("ccw");
    break;
  case up:
    sendMessage("up");
    break;
  case down:
    sendMessage("down");
    break;
  default:
    sendMessage("hallo");
    break;
  }
}

void loop()
{
  int cwButtonState = digitalRead(cwButton);
  int ccwButtonState = digitalRead(ccwButton);
  int potentiometerValue = analogRead(potentiometer);
  int switchState = digitalRead(switchPin);
  int xVal = analogRead(xPin);
  int yVal = analogRead(yPin);

  if (isButtonPressed(cwButtonState))
  {
    doStuff(cw);
  }
  if (isButtonPressed(ccwButtonState))
  {
    doStuff(ccw);
  }
  if (isButtonPressed(cwButtonState && ccwButtonState))
  {
    doStuff(stay);
  }
  if (isButtonPressed(switchState) && !inAir)
  {
    doStuff(takeOff);
  }
  if (!isButtonPressed(switchState) && inAir)
  {
    doStuff(land);
  }
  if (potentiometerValue > 3900)
  {
    doStuff(up);
  }
  if (potentiometerValue < 200)
  {
    doStuff(down);
  }
  if (xVal > 3000)
  {
    doStuff(right);
  }
  if (xVal < 1000)
  {
    doStuff(left);
  }
  if (yVal > 3000)
  {
    doStuff(forward);
  }
  if (yVal < 1000)
  {
    doStuff(backward);
  }

  

  delay(100);
}
