package com.or.loggin;

import com.or.enums.ClientType;
import com.or.errors.ApplicationException;
import com.or.errors.LoginMatchingEntityException;
import com.or.facade.AdminFacade;
import com.or.facade.ClientFacade;
import com.or.facade.CompanyFacade;
import com.or.facade.CustomerFacade;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class LoginManager {

    public static final LoginManager instance = new LoginManager();

    private final Set<String> authenticatedUsers = new HashSet<>();
    private final AdminFacade adminFacade;
    private final CompanyFacade companyFacade;
    private final CustomerFacade customerFacade;

    //Creating a private constructor
    private LoginManager() {
        //Injecting the entities instances
        this.adminFacade = AdminFacade.instance;
        this.companyFacade = CompanyFacade.instance;
        this.customerFacade = CustomerFacade.instance;
    }

    //------------------------------------------Entity Login-------------------------------------------------------

    public ClientFacade login(final String email, final String password, final ClientType clientType) throws ApplicationException {

        boolean isAuthenticated;
        ClientFacade facadeType;

        //Verifying which facade to return according to the specific credentials
        switch (clientType) {

            case ADMINISTRATOR:

                facadeType = this.adminFacade;
                break;

            case COMPANY:

                facadeType = this.companyFacade;
                break;

            case CUSTOMER:

                facadeType = this.customerFacade;
                break;

            default:

                throw new LoginMatchingEntityException(email, clientType);
        }

        //Setting the specific facade to our boolean variable
        isAuthenticated = facadeType.login(email, password);

        if (isAuthenticated) {
            //Adding the email address to the set list
            authenticatedUsers.add(email);
            return facadeType;
        }
        throw new LoginMatchingEntityException(email, clientType);
    }
}




