package numeri;

public class Test extends Thread{
    public static void main(String [] args){
        //Oggetti
        ProduttoreConsumatore p1=new ProduttoreConsumatore(true,1); //Produttore ---> isProduttore: true
        ProduttoreConsumatore p2=new ProduttoreConsumatore(false,3); //Consumatore ---> isProduttore: false

        //Avvio
        p1.start();
        p2.start();
    }
}
