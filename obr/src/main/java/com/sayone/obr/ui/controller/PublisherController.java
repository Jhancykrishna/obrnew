package com.sayone.obr.ui.controller;

import com.sayone.obr.model.request.PublisherDetailsRequestModel;
import com.sayone.obr.model.response.PublisherRestModel;
import com.sayone.obr.service.PublisherService;
import com.sayone.obr.shared.dto.PublisherDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("publisher")
public class PublisherController {

    @Autowired
    PublisherService publisherService;

    @GetMapping("/{id}")
    public PublisherRestModel getPublisher(@PathVariable String id) {

        PublisherRestModel returnValue = new PublisherRestModel();

        PublisherDto publisherDto = publisherService.getPublisherByPublisherId(id);
        BeanUtils.copyProperties(publisherDto, returnValue);

        return returnValue;
    }

    @PostMapping
    public PublisherRestModel createPublisher(@RequestBody PublisherDetailsRequestModel publisherDetails) {

        PublisherRestModel returnValue = new PublisherRestModel();

        PublisherDto publisherDto = new PublisherDto();
        BeanUtils.copyProperties(publisherDetails, publisherDto);

        PublisherDto createdPublisher = publisherService.createPublisher(publisherDto);
        BeanUtils.copyProperties(createdPublisher, returnValue);

        return returnValue;
    }

    @PutMapping(path = "/{id}")
    public PublisherRestModel updatePublisher(@PathVariable String id, @RequestBody PublisherDetailsRequestModel publisherDetails) {

        PublisherRestModel returnValue = new PublisherRestModel();

        PublisherDto publisherDto = new PublisherDto();
        BeanUtils.copyProperties(publisherDetails, publisherDto);

        PublisherDto updatedPublisher = publisherService.updatePublisher(id, publisherDto);
        BeanUtils.copyProperties(updatedPublisher, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}")
    public void deletePublisher(@PathVariable String id) {

        publisherService.deletePublisher(id);
    }
}
