package eu.organicity.data.controller;

import com.amaxilatis.orion.model.OrionContextElementWrapper;
import com.amaxilatis.orion.model.SubscriptionUpdate;
import eu.organicity.data.service.StorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(value = {"/api/v1", "/v1"})
public class ContextController extends BaseController {

    protected static final Logger LOGGER = Logger.getLogger(ContextController.class);

    @Autowired
    StorageService storageService;

    @PostConstruct
    public void init() {
    }

    /**
     * A method that handles subscription updates from Orion.
     * <p>
     *
     * @param update the {@link SubscriptionUpdate} received from the Orion Context Broker.
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/v1/notifyContext"}, method = RequestMethod.POST, produces = APPLICATION_JSON)
    SubscriptionUpdate notifyContext(@RequestBody final SubscriptionUpdate update, @PathVariable("contextConnectionId") String contextConnectionId) {
        LOGGER.debug("[call] notifyContext " + update.getSubscriptionId());

        for (final OrionContextElementWrapper wrapper : update.getContextResponses()) {
            storageService.storeUpdate(wrapper.getContextElement());
        }

        return update;
    }

}