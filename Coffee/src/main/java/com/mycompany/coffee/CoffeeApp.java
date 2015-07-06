
package com.mycompany.coffee;









import coffeeLibrary.AnimalModule;
import coffeeLibrary.Dog;
import dagger.Lazy;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;




public class CoffeeApp implements Runnable {
    @Inject CoffeeMaker coffeeMaker;
    @Inject Dog dog;
    
    @Override public void run() {
        
        coffeeMaker.brew();
        dog.bark();
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(CoffeeApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        ObjectGraph objectGraph = ObjectGraph.create(new DripCoffeeModule());
        CoffeeApp coffeeApp = objectGraph.get(CoffeeApp.class);
        coffeeApp.run();
    }
}

class CoffeeMaker {
    @Inject Lazy<Heater> heater; // Don't want to create a possibly costly heater until we need it.
    @Inject Pump pump;
    
    public void brew() {
        heater.get().on();
        pump.pump();
        System.out.println(" [_]P coffee! [_]P ");
        heater.get().off();
    }
}

@Module(
        injects = CoffeeApp.class,
        includes = {AnimalModule.class, PumpModule.class}
)
        
        class DripCoffeeModule {
    @Provides @Singleton Heater provideHeater() {
        return new ElectricHeater();
    }
}

class ElectricHeater implements Heater {
    boolean heating;
    
    @Override public void on() {
        System.out.println("~ ~ ~ heating ~ ~ ~");
        this.heating = true;
    }
    
    @Override public void off() {
        this.heating = false;
    }
    
    @Override public boolean isHot() {
        return heating;
    }
}

interface Heater {
    void on();
    void off();
    boolean isHot();
}

@Module(complete = false, library = true)
        class PumpModule {
    @Provides Pump providePump(Thermosiphon pump) {
        return pump;
    }
}

interface Pump {
    void pump();
}

class Thermosiphon implements Pump {
    private final Heater heater;
    
    @Inject
          Thermosiphon(Heater heater) {
              this.heater = heater;
          }
          
          @Override public void pump() {
              if (heater.isHot()) {
                  System.out.println("=> => pumping => =>");
              }
          }
}
