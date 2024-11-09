package numeri;

import java.util.concurrent.TimeUnit;

public class ProduttoreConsumatore extends Thread {
    private static int [] arr=new int[10];
    private static Integer lastP=-1;
    private static Integer startC=-1;

    private boolean isProduttore;

    public ProduttoreConsumatore (boolean isProduttore) {
        this.isProduttore=isProduttore;
    }

    public void produco() {
        try {
            //Pausa
            TimeUnit.SECONDS.sleep(1);
            //Controllo se siamo alla fine dell'array
            if (lastP+1==arr.length)
                lastP-= arr.length;
            //Produco
            if(arr[lastP+1] == 0) {
                arr[++lastP]++;
                System.out.println("Ultimo prodotto: " + lastP);
            }
            //Controllo presenza di posizioni per scrittura o mi metto in attesa
            if (lastP == startC && (lastP!=-1 && startC!=-1)) {
                synchronized (lastP) {
                    lastP.wait();
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    public void consumo() {
        try {
            //Pausa
            TimeUnit.SECONDS.sleep(5);
            //Controllo se siamo alla fine dell'array
            if (startC+1==arr.length)
                startC-= arr.length;
            //Produco
            if(arr[startC+1] == 1) {
                arr[++startC]--;
                System.out.println("Ultimo consumato: " + startC);
            }
            //Controllo presenza di posizioni per scrittura ed in caso risveglio chi si fosse messo in attesa
            if (lastP == startC-1 || (lastP-arr.length) == startC-1) {
                synchronized (lastP) {
                    lastP.notify();
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