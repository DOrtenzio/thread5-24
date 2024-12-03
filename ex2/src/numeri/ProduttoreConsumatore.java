package numeri;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ProduttoreConsumatore extends Thread {
    private static Integer [] arr=new Integer[5];
    private static Integer lastP=-1;
    private static Integer lastC =-1;
    private static boolean isPrimaEsecuzione=true;

    private final int velocita;
    private boolean isProduttore;

    public ProduttoreConsumatore (boolean isProduttore, int velocita) {
        this.isProduttore=isProduttore;
        this.velocita=velocita;
        Arrays.fill(arr,-1);
    }

    public void produco() {
        try {
            //Pausa
            TimeUnit.SECONDS.sleep(this.velocita);

            if (lastC == lastP || (lastC - arr.length) == lastP) {
                synchronized (lastC) {
                    lastC.notify();
                }
            }

            //Produco
            if((lastP + 1) % arr.length != lastC) {
                lastP=(lastP + 1) % arr.length;
                synchronized (arr[lastP]){
                    arr[lastP]= (int) (Math.random()*10);
                }
                System.out.println("Ultimo prodotto in posizione: " + lastP +" prodotto: "+arr[lastP]);
            } else{
                lastP=(lastP + 1) % arr.length;
                synchronized (arr[lastP]) {
                    arr[lastP] = (int) (Math.random() * 10);
                }
                System.out.println("Ultimo prodotto in posizione: " + lastP + " prodotto: " + arr[lastP]);
                synchronized (lastP) {
                    lastP.wait();
                }
            }

            if (isPrimaEsecuzione){
                synchronized (lastC){
                    lastC.notify();
                }
                isPrimaEsecuzione=false;
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    public void consumo() {
        try {
            //Pausa
            TimeUnit.SECONDS.sleep(this.velocita);

            if (isPrimaEsecuzione){
                synchronized (lastC){
                    lastC.wait();
                }
                isPrimaEsecuzione=false;
            }
            
            if (lastC == lastP || (lastP - arr.length) == lastC) {
                synchronized (lastC) {
                    lastC.notify();
                }
            }

            //Consumo

            if((lastC + 1) % arr.length != lastP) {
                lastC = (lastC + 1) % arr.length;
                synchronized (arr[lastC]) {
                    arr[lastC] = -1;
                }
                System.out.println("\t\t\tUltimo consumato in posizione: " + lastC+" consumato: "+arr[lastC]);
            }else{
                lastC = (lastC + 1) % arr.length;
                synchronized (arr[lastC]){
                    arr[lastC]=-1;
                }
                System.out.println("\t\t\tUltimo consumato in posizione: " + lastC+" consumato: "+arr[lastC]);
                synchronized (lastC){
                    lastC.wait();
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    public void run() {
        while(true) {
            if (this.isProduttore)
                produco();
            else
                consumo();
        }
    }
}
