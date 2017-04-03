/* mbed Microcontroller Library
 * Copyright (c) 2006-2013 ARM Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include <map>
#include <cstdint>
#include <inttypes.h>

#include "mbed.h"
#include "ble/BLE.h"
#include "ble/DiscoveredCharacteristic.h"
#include "ble/DiscoveredService.h"

#define SCAN_INT  0x14 // 20 ms 
#define SCAN_WIND 0xa // 10 ms 
#define SCAN_TOUT 0x1


const Gap::Address_t  BLE_address_BE       = {0xCC, 0x00, 0x00, 0xE1, 0x80, 0x02};
/*const Gap::Address_t  BLE_peer_address_BE  = {0x80, 0x66, 0x05, 0x13, 0xBE, 0xBA};
const Gap::Address_t  BLE_peer_address_BE2  = {0x87, 0x66, 0x05, 0x13, 0xBE, 0xBA};
*/
DiscoveredCharacteristic ledCharacteristic;
DigitalOut led1(LED2);
Serial pc(SERIAL_TX, SERIAL_RX);
std::map<uint64_t,double> recent_scanned;
std::map<uint64_t,double> nb_scan_per_device;
const char* Name = "UnNamedDevice";
const char* ThisID = "MB1136C-02214344161";
uint8_t toggledValue = 0;
enum {
  READ = 0,
  WRITE,
  IDLE
};
static volatile unsigned int triggerOp = IDLE;
void timeOutCallback(const Gap::TimeoutSource_t params){
    //if (params == Gap::TIMEOUT_SRC_SCAN){
        pc.printf("TOOOOT TOOOOT MADAFUKA ");
      //  }
}
    
void disconnectionCallback(const Gap::DisconnectionCallbackParams_t *params)
{
    (void)params;
    pc.printf("disconnected\n\r");
}

void advertisementCallback(const Gap::AdvertisementCallbackParams_t *params) {
    /*if (params->peerAddr[0] != BLE_peer_address_BE[0]) {
        pc.printf("qqchose");
        pc.printf("%i \r\n", params->peerAddr[0]);
        return;
    }*/
    //if(params->isScanResponse){
        
        double pwr =  (double) params->rssi;//expm1((double)params->rssi);
        double exped = exp(pwr);
        double loged = pow(2.0,(pwr/6.0));
        double dived = 1/(-pwr);
        //ligne a modifier pour tester les diverses précisions
        double toPrint = loged;
        
        uint64_t flatID = params->peerAddr[5]*256;
        flatID = (flatID + params->peerAddr[4])*256;
        flatID = (flatID + params->peerAddr[3])*256;
        flatID = (flatID + params->peerAddr[2])*256;
        flatID = (flatID + params->peerAddr[1])*256;
        flatID = (flatID + params->peerAddr[0]);
        if(nb_scan_per_device.find(flatID) == nb_scan_per_device.end()){
           /* pc.printf("==================================\r\n");
            char name[15];
            bool isNamed = false;
            if(params->advertisingDataLen > 0){
                pc.printf(" lolololol \r\n");
                int j =0;
                for(int i = 1; i<params->advertisingDataLen;i++){
                    int c = params->advertisingData[i];
                    if((c>='A' && c<='Z') || (c>='a' && c<='z')){
                        isNamed = true;
                        pc.printf("%c",c);
                        name[j] = c;
                        j++;
                        if (j == 15)
                            break;
                    }
                }
                name[j] = '\0';
                pc.printf("%s",name);
                pc.printf("==================================\r\n");
            }
            */
            nb_scan_per_device[flatID] = 0;
            recent_scanned[flatID] = toPrint;
         /*   if(isNamed)
                pc.printf("new : \"%s \" adv peerAddr[%12x] rssi %lf, isScanResponse %u, AdvertisementType %u\r\n",name,
                    flatID, dived, params->isScanResponse, params->type);
            else */
          //      pc.printf("new : %s ",UnNamed);
            pc.printf(">%s#",ThisID);
            pc.printf("%12x#%lf#%s\r\n",flatID, toPrint, Name);
        }
        else if(recent_scanned.find(flatID) != recent_scanned.end()){
            if((recent_scanned[flatID] < toPrint*0.8) ||(recent_scanned[flatID] > toPrint*1.2)){
                recent_scanned[flatID] = toPrint;
                pc.printf(">%s#",ThisID);
                pc.printf("%12x#%lf#%s\r\n",flatID, toPrint, Name);
            }
        }
        else{
            recent_scanned[flatID] = toPrint;
            pc.printf(">%s#",ThisID);
            pc.printf("%12x#%lf#%s\r\n",flatID, toPrint, Name);
        }
        nb_scan_per_device[flatID]++;
        
        //TODO : selon l'adresse du l'emmeteur, transmettre adresse + id + force signal;
    //if(!params->isScanResponse) {
    //  BLE::Instance().gap().connect(params->peerAddr, Gap::ADDR_TYPE_PUBLIC, NULL, NULL);
    //}
}

void discoveryTerminationCallback(Gap::Handle_t connectionHandle) {
    pc.printf("terminated SD for handle %u\r\n", connectionHandle);
}

void serviceDiscoveryCallback(const DiscoveredService *service) {
    if (service->getUUID().shortOrLong() == UUID::UUID_TYPE_SHORT) {
        pc.printf("S UUID-%x attrs[%u %u]\r\n", service->getUUID().getShortUUID(), service->getStartHandle(), service->getEndHandle());
    } else {
        pc.printf("S UUID-");
        const uint8_t *longUUIDBytes = service->getUUID().getBaseUUID();
        for (unsigned i = 0; i < UUID::LENGTH_OF_LONG_UUID; i++) {
            pc.printf("%02X", longUUIDBytes[i]);
        }
        pc.printf(" attrs[%u %u]\r\n", service->getStartHandle(), service->getEndHandle());
    }
}
 
void characteristicDiscoveryCallback(const DiscoveredCharacteristic *characteristicP) {
    //pc.printf("  C UUID-%x valueAttr[%u] props[%x]\r\n", characteristicP->getShortUUID(), characteristicP->getValueHandle(), (uint8_t)characteristicP->getProperties().broadcast());
    if (characteristicP->getUUID().getShortUUID() == 0xa001) { /* !ALERT! Alter this filter to suit your device. */
      //pc.printf("  C UUID-%x valueAttr[%u] props[%x]\r\n", characteristicP->getShortUUID(), characteristicP->getValueHandle(), (uint8_t)characteristicP->getProperties().broadcast());
      ledCharacteristic = *characteristicP;
      triggerOp = READ;
    }
}

void connectionCallback(const Gap::ConnectionCallbackParams_t *params) {
  uint16_t LED_SERVICE_UUID = 0xA000;
  uint16_t LED_STATE_CHARACTERISTIC_UUID = 0xA001;
  pc.printf(" On se connecte?\r\n");
  if (params->role == Gap::CENTRAL) {
    BLE &ble = BLE::Instance();
    ble.gattClient().onServiceDiscoveryTermination(discoveryTerminationCallback);
    ble.gattClient().launchServiceDiscovery(params->handle, serviceDiscoveryCallback, characteristicDiscoveryCallback, LED_SERVICE_UUID, LED_STATE_CHARACTERISTIC_UUID);
  }
}

void triggerToggledWrite(const GattReadCallbackParams *response) {
  if (response->handle == ledCharacteristic.getValueHandle()) {
#if 0
    pc.printf("triggerToggledWrite: handle %u, offset %u, len %u\r\n", response->handle, response->offset, response->len);
    for (unsigned index = 0; index < response->len; index++) {
      pc.printf("%c[%02x]", response->data[index], response->data[index]);
    }
    pc.printf("\r\n");
#endif
    
    toggledValue = response->data[0] ^ 0x1;
    triggerOp = WRITE;
  }
}

void triggerRead(const GattWriteCallbackParams *response) {
  if (response->handle == ledCharacteristic.getValueHandle()) {
    triggerOp = READ;
  }
}

/** 
 * This function is called when the ble initialization process has failled 
 */ 
void onBleInitError(BLE &ble, ble_error_t error) 
{ 
    /* Initialization error handling should go here */ 
}

void tickercallback(void){
    recent_scanned.clear();
}
/** 
 * Callback triggered when the ble initialization process has finished 
 */ 
void bleInitComplete(BLE::InitializationCompleteCallbackContext *params) 
{
    BLE&        ble   = params->ble;
    ble_error_t error = params->error;

    if (error != BLE_ERROR_NONE) {
        /* In case of error, forward the error handling to onBleInitError */
        onBleInitError(ble, error);
        return;
    }

    /* Ensure that it is the default instance of BLE */
    if(ble.getInstanceID() != BLE::DEFAULT_INSTANCE) {
        return;
    }
    Ticker myticker;
    myticker.attach(tickercallback,10);
    // Set BT Address
    ble.gap().setAddress(BLEProtocol::AddressType::PUBLIC, BLE_address_BE);

    ble.gap().onConnection(connectionCallback);
    ble.gap().onTimeout(timeOutCallback);
    ble.onDisconnection(disconnectionCallback);
       
    ble.gattClient().onDataRead(triggerToggledWrite);
    ble.gattClient().onDataWrite(triggerRead);

    ble.gap().setScanParams(SCAN_INT, SCAN_WIND);
    ble.gap().setScanTimeout(SCAN_TOUT);
    ble.gap().startScan(advertisementCallback);
    // infinite loop
    while (1) {
      ble.waitForEvent();
    }
}

int main(void)
{
    BLE::Instance().init(bleInitComplete);
}

