#include <Arduino.h>
#include "WiFi.h"
#include "AsyncUDP.h"

const char * ssid = "lego-robot";
const char * password = "lego-2016";


enum Movement{
  left, right, forward, backward, stay, up, down, land, takeOff, cw, ccw

};
bool inAir = false;

const int switchPin = 17;
const int drejeTingPin = 13;
const int xPin = 21;
const int yPin = 2;
const int button = 04;
const int ccwButton = 14;
const int cwButton = 15;

String speed = "21";

AsyncUDP udp;


void setup() {
  
  pinMode(switchPin, INPUT_PULLDOWN);
  pinMode(drejeTingPin, INPUT);
  pinMode(button, INPUT_PULLUP);
  pinMode(cwButton, INPUT_PULLDOWN);
  pinMode(ccwButton, INPUT_PULLDOWN);
  pinMode(xPin, INPUT);
  pinMode(yPin, INPUT);  
  Serial.begin(9600);
/*
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
*/
 
}


void sendMessage(String msg){
  udp.writeTo((const uint8_t *)msg.c_str(), msg.length(),
              IPAddress(192, 168, 1, 17), 4001);


}



bool isInDeadZone(std::pair<int,int> joystick) {
  int joyXCenterVal = 1715;
  int joyYCenterVal = 1776;
  int offset = 150;
  bool deadX = joystick.first < joyXCenterVal + offset && joystick.first > joyXCenterVal - offset;
  bool deadY = joystick.second < joyYCenterVal + offset && joystick.second > joyYCenterVal - offset;
  return deadX && deadY;
}

void formatPrintJoystickValues(std::pair<int,int> joystick) {
  String s = "X: " + String(joystick.first) + ", Y: " + String(joystick.second);
  Serial.println(s);
}

std::pair<int, int> getJoystickValues() {
  int xVal = analogRead(xPin);
  int yVal = analogRead(yPin);
  return std::make_pair(xVal, yVal);
}

bool isButtonPressed(int buttonState){
 if (buttonState == 0){
   return true;
 } else {
   return false;
 }
  
}

void doStuff(Movement movement){
switch (movement){
  case left:
  sendMessage("left " + speed );
  break;
  case right:
  sendMessage("right " + speed);
  break;
  case forward:
  sendMessage("forward " + speed);
  break;
  case backward:
  sendMessage("back " + speed);
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
  sendMessage("cw " + speed);
  break;
  case ccw:
  sendMessage("ccw " + speed);
  break;
  case up:
  sendMessage("up " + speed);
  break;
  case down:
  sendMessage("down " + speed);
  break;
}
}


void loop() {
int buttonState = digitalRead(button);
int cwButtonState = digitalRead(cwButton);
int ccwButtonState = digitalRead(ccwButton);
int drejeTingValue = analogRead(drejeTingPin);
int switchState = digitalRead(switchPin);
  int xVal = analogRead(xPin);
  int yVal = analogRead(yPin);

if (isButtonPressed(cwButtonState)){                     doStuff(cw);}
if (isButtonPressed(ccwButtonState)){                    doStuff(ccw);}
if (isButtonPressed(cwButtonState && ccwButtonState)){   doStuff(stay);}
if (isButtonPressed(switchState) && !inAir){             doStuff(takeOff);}
if (!isButtonPressed(switchState) && inAir){             doStuff(land);}
if (drejeTingValue > 3900){                              doStuff(up);}
if (drejeTingValue < 200){                               doStuff(down);}
if (xVal > 3000){                                        doStuff(right);}
if (xVal < 1000){                                        doStuff(left);} 
if (yVal > 3000){                                        doStuff(forward);}
if (yVal < 1000){                                        doStuff(backward);}




  
  //sendMessage("THOMAAAÅAS");
  delay(1000);
 
 
 

formatPrintJoystickValues(getJoystickValues());

Serial.println(cwButtonState);
//Serial.println(cwButtonState);



 delay(200);

}