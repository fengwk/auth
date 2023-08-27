package fun.fengwk.auth.core.facade;

import fun.fengwk.auth.core.model.ClientBO;

/**
 * @author fengwk
 */
public interface ClientFacade {

    ClientBO getByClientId(String clientId);

}
