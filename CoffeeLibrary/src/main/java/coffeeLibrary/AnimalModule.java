/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coffeeLibrary;

import dagger.Module;
import dagger.Provides;

/**
 *
 * @author Jeffrey Agustin
 */
@Module(complete = false, library = true)
public class AnimalModule {
    @Provides Dog providesDog(){
        return new DogImpl();
    }
}
