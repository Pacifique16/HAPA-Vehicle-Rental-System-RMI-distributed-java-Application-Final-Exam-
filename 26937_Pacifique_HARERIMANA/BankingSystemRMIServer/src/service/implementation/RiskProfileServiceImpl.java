package service.implementation;

import dao.RiskProfileDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.RiskProfile;
import service.RiskProfileService;

public class RiskProfileServiceImpl extends UnicastRemoteObject implements RiskProfileService{

    RiskProfileDao dao = new RiskProfileDao();
    public RiskProfileServiceImpl() throws RemoteException{
    }

    @Override
    public RiskProfile registerRiskProfile(RiskProfile theRiskProfile) throws RemoteException {
        return dao.createRiskProfile(theRiskProfile);
    }

    @Override
    public RiskProfile updateRiskProfile(RiskProfile theRiskProfile) throws RemoteException {
        return dao.updateRiskProfile(theRiskProfile);
    }

    @Override
    public RiskProfile deleteRiskProfile(RiskProfile theRiskProfile) throws RemoteException {
        return dao.deleteRiskProfile(theRiskProfile);
    }

    @Override
    public RiskProfile searchRiskProfileById(RiskProfile theRiskProfile) throws RemoteException {
        return dao.findRiskProfileById(theRiskProfile);
    }

    @Override
    public List<RiskProfile> retrieveAllRiskProfiles() throws RemoteException {
        return dao.findAllRiskProfiles();
    }
}
