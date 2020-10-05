package com.crossingminds.xminds.api;

import com.crossingminds.xminds.api.exception.XmindsException;
import com.crossingminds.xminds.api.model.IndividualAccount;
import com.crossingminds.xminds.api.model.Organization;
import com.crossingminds.xminds.api.model.RootAccount;
import com.crossingminds.xminds.api.model.ServiceAccount;
import com.crossingminds.xminds.api.model.Token;

public interface Endpoint {

	Organization listAllAccounts() throws XmindsException;

	IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XmindsException;

	void deleteIndividualAccount(IndividualAccount individualAccount) throws XmindsException;

	Token loginRoot(RootAccount rootAccount) throws XmindsException;

	Token loginRefreshToken() throws XmindsException;

	ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XmindsException;

	void deleteServiceAccount(ServiceAccount serviceAccount) throws XmindsException;

	Token loginIndividual(IndividualAccount individualAccount) throws XmindsException;

	Token loginService(ServiceAccount serviceAccount) throws XmindsException;

	void resendVerificationCode(String email) throws XmindsException;

	void verifyAccount(String code, String email) throws XmindsException;

	void deleteCurrentAccount() throws XmindsException;

}
