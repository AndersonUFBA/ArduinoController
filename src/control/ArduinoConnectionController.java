/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;


import static jssc.SerialPort.MASK_RXCHAR;

import java.util.ArrayList;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import model.ArduinoConnection;


public class ArduinoConnectionController {
    List<String> portList;
    
    private ArduinoConnectionController() {
        detectPort();
    }

    public static ArduinoConnectionController getInstance(){
        return new ArduinoConnectionController();
    }
    
    public List<String> getPortList() {
        return portList;
    }
    
    private void detectPort(){
         
        portList = new ArrayList<>();
 
        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            portList.add(name);
        }
    }
    
    public ArduinoConnection connectArduino(String port) throws SerialPortException{
                
        @SuppressWarnings("unused")
		boolean success = false;
        SerialPort serialPort = new SerialPort(port);
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if(serialPortEvent.isRXCHAR()){
                    try {
                        
                        byte[] b = serialPort.readBytes();
                        int value = b[0] & 0xff;    //convert to int
                        @SuppressWarnings("unused")
						String st = String.valueOf(value);                         

                        
                    } catch (SerialPortException ex) {
                    }
                }
            });
            return new ArduinoConnection(serialPort);

    }
}
