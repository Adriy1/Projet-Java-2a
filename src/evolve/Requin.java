import gui.*;
import java.awt.*;
import java.util.*;

public class Requin extends Boid {

  public Requin(int x, int y,int taille) {
    super(x,y,taille);
    this.vmax = (new Gauss(10,50,10)).tirage();
    this.v = new Vector((int)((Math.random()-0.5)*this.vmax),(int)((Math.random()-0.5)*this.vmax));
    this.sz= 22 - vmax/3;
    this.vision = (new Gauss(200,400,50)).tirage();
  }

  public Vector separation(ArrayList<Requin> tabRequin) {
    Vector v2 = new Vector(0,0);
    Vector w = new Vector(0,0);
    int c=1;
    for( Requin e : tabRequin) {
      if (this.equal(e) != true) {
        w.x = (int) (e.p.getX() - this.p.getX());
        w.y = (int) (e.p.getY() - this.p.getY());  // w = position du boid e - position du boid j
        if (w.norm() < 2*(this.sz+e.sz) ) {
          v2.sous(w);
           // si la norme de w < a une valeur alors v2 = - w
          c++;
        }
      }
    }
    v2.mult((double) 1/2*c);
    return v2;
  }

  public void manger(ArrayList<Poisson> tabPoisson) {
    ArrayList<Poisson> indice = new ArrayList<Poisson>();
    for( Poisson e : tabPoisson) {
      Vector w = new Vector( (int) (e.p.getX()-this.p.getX()), (int)(e.p.getY()-this.p.getY()));  //on crée un vecteur entre e et le poisson j
      if(this.v.getAngle(w)<90 && w.norm()< 2*this.sz){ //on test si il est proche du requin et avec un angle de 180 degres
        indice.add(e); //on ajoute les indices des poisson a supprimer (on ne peut pas supprimer des elements d'ne liste en la parcourant)
        sz++;
        if(vmax > 5){
          vmax--;
        }
      }
    }
    for(Poisson i : indice){
      tabPoisson.remove(i);
    }
  }

  public Vector chasser(ArrayList<Poisson> tabPoisson){
    Vector v1 = new Vector((int)this.p.getX(),(int)this.p.getY());
    int nbpoissonvu = 0;
      for( Poisson e : tabPoisson) {
          Vector w = new Vector( (int) (e.p.getX()-this.p.getX()), (int)(e.p.getY()-this.p.getY()));  //on crée un vecteur entre e et le boidj
          if(this.v.getAngle(w)<90 && w.norm()<vision){ //on test si il est dans le champs de vision de longeur 200 et avec un angle de 180 degres
            Vector z = new Vector((int) e.p.getX(),(int)e.p.getY());
            v1.plus(z); //on somme tous les vecteurs positions pour les boids pour obtenir une moyenne percue
            nbpoissonvu++;
          }
      }
      if (nbpoissonvu == 0) {
        v1.sous(new Vector((int)this.p.getX(),(int)this.p.getY()));
        return v1;
      }
      else {
        v1.mult((double)1/(nbpoissonvu)); // on fait la moyenne
        v1.sous(new Vector((int)this.p.getX(),(int)this.p.getY())); // on calcule le vecteur qui va du boidj au point moyen percue
        v1.mult(0.08); //facteur pour limiter l'influence
        return v1;
      }
  }


  public void afficheRequin(GUISimulator gui){
    gui.addGraphicalElement(new Oval((int) p.getX(),(int) p.getY(),Color.blue,Color.black,3*sz));
    gui.addGraphicalElement(new Oval((int) p.getX(),(int) p.getY(),Color.blue,Color.blue,sz));
    gui.addGraphicalElement(new gui.Rectangle((int) (p.getX()+v.x),(int) (p.getY()+v.y) ,Color.blue,Color.blue,10)); // permet de representer le vecteur vitesse
  }

}