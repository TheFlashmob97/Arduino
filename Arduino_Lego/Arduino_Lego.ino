int AA = 4;
int AB = 5;
int AC = 6;
int AD = 7; 

int ct[9999]; //= {1,2000,3,90,1,5000};
int x;

void setup() {
  pinMode(AA,OUTPUT);
  pinMode(AB,OUTPUT);
  pinMode(AC,OUTPUT);
  pinMode(AD,OUTPUT);
}

void percorso(int i){
  switch(ct[i]){
    case 1:
      i++;
      x = ct[i];
      avanti();
      delay(x);
      break;
    case 2:
      i++;
      x = ct[i];
      indietro();
      delay(x);
      break;
    case 3:
      i++;
      x = ct[i];
      destra();
      delay(x);
      break;
    case 4:
      i++;
      x = ct[i];
      sinistra();
      delay(x);
      break;
  }
}

void loop() {
  int i = 0;
  percorso(i);
  i++;
  i++;
}

void sinistra(){
  digitalWrite(AA, HIGH);
  digitalWrite(AB, LOW);
  digitalWrite(AC, HIGH);
  digitalWrite(AD, LOW);
}

void destra(){
  digitalWrite(AA, LOW);
  digitalWrite(AB, HIGH);
  digitalWrite(AC, LOW);
  digitalWrite(AD, HIGH);
}

void avanti(){
  digitalWrite(AA, LOW);
  digitalWrite(AB, HIGH);
  digitalWrite(AC, HIGH);
  digitalWrite(AD, LOW);
}

void indietro(){
  digitalWrite(AA, HIGH);
  digitalWrite(AB, LOW);
  digitalWrite(AC, LOW);
  digitalWrite(AD, HIGH);
}

void ferma(){
  digitalWrite(AA, LOW);
  digitalWrite(AB, LOW);
  digitalWrite(AC, LOW);
  digitalWrite(AD, LOW);
}
