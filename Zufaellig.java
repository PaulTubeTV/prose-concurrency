public void zufällig(Konto k, int n){
    for(int i = 0; i < n; i++){
        int a = rand.nextInt(2);
        if(a == 0) k.einzahlen(rand.nextInt(20));
        if(a == 1) k.auszahlen(rand.nextInt(20));
    }
}