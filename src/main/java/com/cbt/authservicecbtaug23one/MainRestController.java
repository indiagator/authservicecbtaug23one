package com.cbt.authservicecbtaug23one;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class MainRestController
{

    Logger logger = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    FullUserDetailService fullUserDetailService;

    @Autowired
    UserdetailRepository userdetailRepository;

    @Autowired
    UserportlinkRepository userportlinkRepository;

    @Autowired
    UsertypelinkRepository usertypelinkRepository;

    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    Producer producer;

    @GetMapping("get/user/detail/{username}")
    public ResponseEntity<FullUserDetail> getUserDetail(@PathVariable("username") String username)
    {
        return new ResponseEntity<>(fullUserDetailService.composeFullUserDetail(username), HttpStatus.OK);
    }

    @PostMapping("save/user/detail")
    public ResponseEntity<Userdetail> saveUserDetails(@RequestBody Userdetail userdetail) throws JsonProcessingException {
        userdetailRepository.save(userdetail);

        logger.info("NEW DATUM PUBLISHED");

        producer.sendAnalyticPayload("USERDETAIL","USER_DETAILS_SAVED",(Userdetail)userdetail);

        return new ResponseEntity<>(userdetail,HttpStatus.OK);
    }

    @PostMapping("save/user/port")
    public ResponseEntity<Userportlink> saveUserPort(@RequestBody Userportlink userportlink)
    {
        userportlink.setLinkid(String.valueOf((int)(Math.random()*100000)));
        userportlinkRepository.save(userportlink);
        return new ResponseEntity<>(userportlink,HttpStatus.OK);
    }

    @PostMapping("save/user/type")
    public ResponseEntity<Usertypelink> saveUserType(@RequestParam String username, @RequestParam String type)
    {
        Usertypelink link = new Usertypelink();
        link.setLinkid(String.valueOf((int)(Math.random()*100000)));
        link.setUsername(username);
        link.setType(type);
        usertypelinkRepository.save(link);
        return new ResponseEntity<>(link,HttpStatus.OK);
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody Credential credential) throws JsonProcessingException {

        credentialRepository.save(credential);

        logger.info("NEW DATUM PUBLISHED");

        AnalyticsDatum datum = new AnalyticsDatum();
        datum.setType("CREDENTIAL");
        datum.setMessage("NEW_SIGNUP_SUCCESSFUL");
        datum.setPayload("CLASSIFIED");

        ObjectMapper mapper = new ObjectMapper();
        String message =  mapper.writeValueAsString(datum);

        producer.sendMessage(message);

        return new ResponseEntity<>("New Signup Successful", HttpStatus.OK);
    }
}
