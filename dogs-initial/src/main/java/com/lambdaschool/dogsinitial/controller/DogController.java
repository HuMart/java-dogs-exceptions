package com.lambdaschool.dogsinitial.controller;

import com.lambdaschool.dogsinitial.exception.ResourceNotFoundException;
import com.lambdaschool.dogsinitial.model.Dog;
import com.lambdaschool.dogsinitial.DogsinitialApplication;
import com.lambdaschool.dogsinitial.model.MessageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;

@Service
@RestController
@RequestMapping("/dogs")
public class DogController
{
    private static final Logger logger = LoggerFactory.getLogger(DogController.class);

    @Autowired
    RabbitTemplate rt;
    //localhost:2019/dogs/dogs
    @GetMapping(value = "/dogs")
    public ResponseEntity<?> getAllDogs()
    {
        logger.info("/dogs/dogs accesed");

        return new ResponseEntity<>(DogsinitialApplication.ourDogList.dogList, HttpStatus.OK);
    }


    // localhost:2019/dogs/{id}
    @GetMapping(value = "/{id}",
                produces = {"application/json"})
    public ResponseEntity<?> getDogDetail(
            @PathVariable
                    long id)
    {
        logger.info("dogs/ " + id + " accesed");
        MessageDetail message = new MessageDetail("/dogs/ " +id+" accessed", 1, true);
        Dog rtnDog;
        if ((DogsinitialApplication.ourDogList.findDog(d -> (d.getId()) == id)) == null)
        {
            throw new ResourceNotFoundException("Dog with id " + id + " not found");
        } else
        {
            rtnDog = DogsinitialApplication.ourDogList.findDog(d -> (d.getId() == id));
        }
        return new ResponseEntity<>(rtnDog, HttpStatus.OK);
    }

    // localhost:2019/dogs/breeds/{breed}
    @GetMapping(value = "/breeds/{breed}",
                produces = {"application/json"})
    public ResponseEntity<?> getDogBreeds(
            @PathVariable
                    String breed)
    {
        logger.info("dogs/breeds/ " + breed + " accesed");
        ArrayList<Dog> rtnDogs = DogsinitialApplication.ourDogList.
                findDogs(d -> d.getBreed().toUpperCase().equals(breed.toUpperCase()));
        if (rtnDogs.size() == 0)
        {
            throw new ResourceNotFoundException("Dogs of breed " + breed + " not found");
        }
        return new ResponseEntity<>(rtnDogs, HttpStatus.OK);
    }

    @GetMapping(value = "/dogtable")
    public ModelAndView displayDogTable()
    {
        logger.info("dogs/dogtable accesed");
        MessageDetail message = new MessageDetail("/dogs/dogtable accessed", 1, true);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("dogs");
        DogsinitialApplication.ourDogList.dogList.sort((d1, d2) -> (d1.getBreed().compareToIgnoreCase(d2.getBreed())));

        mav.addObject("dogList", DogsinitialApplication.ourDogList.dogList);
        return mav;
    }

    @GetMapping(value = "/aptdogs")
    public ModelAndView displayDogsTable()
    {
        MessageDetail message = new MessageDetail("/dogs/aptdogs accessed", 1, true);
        ArrayList<Dog> aptDogs = DogsinitialApplication.ourDogList.findDogs(d -> d.isApartmentSuitable());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("dogs");
        mav.addObject("dogList", aptDogs);

        return mav;
    }
}
