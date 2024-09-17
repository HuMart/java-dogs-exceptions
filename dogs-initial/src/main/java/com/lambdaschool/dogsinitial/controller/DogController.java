package com.lambdaschool.dogsinitial.controller;

import com.lambdaschool.dogsinitial.DogsinitialApplication;
import com.lambdaschool.dogsinitial.exception.ResourceNotFoundException;
import com.lambdaschool.dogsinitial.model.Dog;
import com.lambdaschool.dogsinitial.model.MessageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/dogs")
public class DogController {
    private static final Logger logger = LoggerFactory.getLogger(DogController.class);

    @Autowired
    RabbitTemplate rt;

    // localhost:2019/dogs/dogs
    @GetMapping(value = "/dogs")
    public ResponseEntity<?> getAllDogs() {
        logger.info("/dogs/dogs was accessed");
        MessageDetail message = new MessageDetail("/dogs/dogs was accessed at " + new Date(), 7, false);
        rt.convertAndSend(DogsinitialApplication.QUEUE_NAME_DOGS, message);

        return new ResponseEntity<>(DogsinitialApplication.ourDogList.dogList, HttpStatus.OK);
    }

    // localhost:2019/dogs/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDogDetail(
            @PathVariable
                    long id) {
        logger.info("/dogs/" + id + " was accessed");
        Dog rtnDog;
        if (DogsinitialApplication.ourDogList.findDog(d -> (d.getId() == id)) == null) {
            throw new ResourceNotFoundException("Dog with ID " + id + " not found.");
        } else {
            rtnDog = DogsinitialApplication.ourDogList.findDog(d -> (d.getId() == id));
        }
        return new ResponseEntity<>(rtnDog, HttpStatus.OK);

    }

    // localhost:2019/dogs/breeds/{breed}
    @GetMapping(value = "/breeds/{breed}")
    public ResponseEntity<?> getDogBreeds(
            @PathVariable
                    String breed) {
        logger.info("/dogs/breeds/" + breed + " was accessed");
        MessageDetail message = new MessageDetail("/dogs/breeds/" + breed + " was accessed at " + new Date(), 7, false);
        rt.convertAndSend(DogsinitialApplication.QUEUE_NAME_DOGS_BREED, message);

        ArrayList<Dog> rtnDogs = DogsinitialApplication.ourDogList.
                findDogs(d -> d.getBreed().toUpperCase().equals(breed.toUpperCase()));
        if (rtnDogs.size() == 0) {
            throw new ResourceNotFoundException("No dogs of breed " + breed + " found.");
        }
        return new ResponseEntity<>(rtnDogs, HttpStatus.OK);
    }

    // localhost:2019
    @GetMapping(value = "/dogstable")
    public ModelAndView displayDogsTable() {
        logger.info("/dogs/dogstable was accessed");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("dogs");
        mav.addObject("dogList", DogsinitialApplication.ourDogList.dogList);

        return mav;
    }

    @GetMapping(value = "/aptdogs")
    public ModelAndView displayAptDogsTable() {
        logger.info("/dogs/aptdogs was accessed");
        ArrayList<Dog> aptDogs = DogsinitialApplication.ourDogList.
                findDogs(d -> d.isApartmentSuitable());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("dogs");
        mav.addObject("dogList", aptDogs);

        return mav;
    }
}