package io.UserService.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.UserService.exceptionhandler.DataNotFoundException;
import io.UserService.exceptionhandler.DuplicateDataNotAllowedException;
import io.UserService.exceptionhandler.NullFormatException;
import io.UserService.model.User;
import io.UserService.model.UserDTO;
import io.UserService.repository.UserRepository;
import io.UserService.utils.CommonUtils;

@Service
public class UserService {
	
	private Logger logger=LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${USER_PAYLOAD_NULL}")
	private String USER_PAYLOAD_NULL;
	
	@Value("${USER_PROFILE_NULL}")
	private String USER_PROFILE_NULL;
	
	@Value("${DUPLICATE_USERID}")
	private String DUPLICATE_USERID;
	
	@Value("${DUPLICATE_USERNAME}")
	private String DUPLICATE_USERNAME;
	
	@Value("${NO_USER_DATA}")
	private String NO_USER_DATA;

    /*
        Responsibility: Service to Register New User Information into Embedded H2 Database.
        Parameters: User Object
        Return Type: CompletableFuture<User>
    */
	public CompletableFuture<User> registerNewUserInfo(User userObj) throws DataNotFoundException, NullFormatException, DuplicateDataNotAllowedException {
		logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
	    try {
	        if (userObj == null) {
	            logger.error(USER_PAYLOAD_NULL);
	            throw new NullFormatException(USER_PAYLOAD_NULL);
	        }
	        if (userObj.getUserid() == null || userObj.getUsername() == null) {
	            logger.error(USER_PROFILE_NULL);
	            throw new NullFormatException(USER_PROFILE_NULL);
	        }
	        Integer requestedUserID = userObj.getUserid();
	        if (userRepository.findById(requestedUserID).isPresent()) {
	            logger.error(DUPLICATE_USERID);
	            throw new DuplicateDataNotAllowedException(DUPLICATE_USERID);
	        }
	        if(userRepository.findByUsername(userObj.getUsername())!=null){
	            logger.error(DUPLICATE_USERNAME);
	            throw new DuplicateDataNotAllowedException(DUPLICATE_USERNAME);
	        }
	        return CompletableFuture.completedFuture(userRepository.save(userObj));
	    } finally {
	        logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
	    }
	}

    /*
        Responsibility: Service to retrieve or fetch all User Information from Embedded H2 DB
        Parameters: 
        Return Type: CompletableFuture<UserDTO> , where UserDTO is a wrapper class or DAO class, holding List of User Objects.
    */
	public CompletableFuture<UserDTO> getAllUserInformation() throws DataNotFoundException {
		logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try {
            List<User> allUserInfo = userRepository.findAll();
            if (allUserInfo.size() == 0) {
                logger.error(NO_USER_DATA);
                throw new DataNotFoundException(NO_USER_DATA);
            }
            return CompletableFuture.completedFuture(new UserDTO(allUserInfo));
        } finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
		
	}

    /*
        Responsibility: Service to retrieve or fetch specific User Information by Userid from Embedded H2 DB.
        Parameters: Integer (Userid)
        Return Type: CompletableFuture<User>
    */
	public CompletableFuture<User> getUserInformationById(Integer id) throws DataNotFoundException, NullFormatException {
		logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try {
            if(id.equals(null)) {
            	logger.error(USER_PROFILE_NULL);
            	throw new NullFormatException(USER_PROFILE_NULL);
            }
            if(!userRepository.findById(id).isPresent()) {
            	logger.error("No Data Present for User:" + id + ". Please try with some other Userid");
                throw new DataNotFoundException("No Data Present for User:" + id + ". Please try with some other Userid");
            }
            return CompletableFuture.completedFuture(userRepository.findById(id).get());
        } finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
		
	}

    /*
        Responsibility: Service to retrieve or fetch specific User Information by Username from Embedded H2 DB.
        Parameters: String (Username)
        Return Type: CompletableFuture<User>
    */
	public CompletableFuture<User> getUserInformationByUsername(String username) throws DataNotFoundException, NullFormatException {
		logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try {
            User userResponseObj = null;
            if(username.equalsIgnoreCase(null)||username.equalsIgnoreCase("")) {
            	logger.error(USER_PROFILE_NULL);
            	throw new NullFormatException(USER_PROFILE_NULL);
            }
            userResponseObj = userRepository.findByUsername(username);
            if(userResponseObj==null){
                logger.error("No Data Present for User:" + username + ". Please try with some other Username");
                throw new DataNotFoundException("No Data Present for User:" + username + ". Please try with some other Username");
            }
            return CompletableFuture.completedFuture(userResponseObj);
        } finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
	}
    
    /*
        Responsibility: Service to generate and export Jasper Report having all User Information fetched from Embedded H2 DB.
        Parameters: String (Report Format: html or pdf)
        Return Type: Object
    */
	public Object exportUserReport(String reportFormat) throws DataNotFoundException {
        logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try{
                List<User> allUserInfo = userRepository.findAll();
                if (allUserInfo.size() == 0) {
                    logger.error(NO_USER_DATA);
                    throw new DataNotFoundException(NO_USER_DATA);
                }
                String reportPath=System.getProperty("user.dir")+File.separator+"Reports"+File.separator+"User"+File.separator+CommonUtils.getFormattedCurrentDateTimeString.apply("yyyy-MM-dd-HH-mm-ss");
                String jasperReportTemplateSource=System.getProperty("user.dir")+File.separator+"src/main/resources/user.jrxml";
                return CommonUtils.generateReport(jasperReportTemplateSource,reportFormat,reportPath,allUserInfo,"UserReport");
            }finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
    }

}
