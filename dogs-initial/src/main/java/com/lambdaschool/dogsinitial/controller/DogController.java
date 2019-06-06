package com.lambdaschool.dogsinitial.controller;

import com.lambdaschool.dogsinitial.services.DogsinitialApplication;
import com.lambdaschool.dogsinitial.exception.ResourceNotFoundException;
import com.lambdaschool.dogsinitial.model.Dog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("/data")
public class DogController
{
    private static final Logger logger = LoggerFactory.getLogger(DogController.class);


    //localhost:2019/data/alldogs
    @GetMapping(value = "/alldogs",
            produces = {"application/json"})
    public ResponseEntity<?> getAllDogs(HttpServletRequest request)
    {
        logger.info(request.getRequestURI() + " accessed");

        DogsinitialApplication.ourDogList.dogList.sort((o1, o2) -> o1.getBreed().compareToIgnoreCase(o2.getBreed()));
        return new ResponseEntity<>(DogsinitialApplication.ourDogList.dogList, HttpStatus.OK);
    }


    // localhost:2019/data/dog/2
    @GetMapping(value = "/dog/{id}",
            produces = {"application/json"})
    public ResponseEntity<?> getDogDetail(HttpServletRequest request,
                                          @PathVariable
                                                  long id) throws ResourceNotFoundException
    {
        logger.trace(request.getRequestURI() + " accessed");

        Dog rtnDog;
        if (DogsinitialApplication.ourDogList.findDog(b -> (b.getId() == id)) == null)
        {
            throw new ResourceNotFoundException("dog with id " + id + " not found");
        } else
        {
            rtnDog = DogsinitialApplication.ourDogList.findDog(b -> (b.getId() == id));
        }
        return new ResponseEntity<>(rtnDog, HttpStatus.OK);
    }

    // localhost:2019/data/dogstable
    @GetMapping(value = "/dogstable")
    public ModelAndView displayDogsTable(HttpServletRequest request)
    {
        logger.trace(request.getRequestURI() + " accessed");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("dogs");
        mav.addObject("dogList", DogsinitialApplication.ourDogList.dogList);

        return mav;
    }
}
