package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.RiskProfile;

public interface RiskProfileService extends Remote{
    public RiskProfile registerRiskProfile(RiskProfile theRiskProfile) throws RemoteException;
    public RiskProfile updateRiskProfile(RiskProfile theRiskProfile) throws RemoteException;
    public RiskProfile deleteRiskProfile(RiskProfile theRiskProfile) throws RemoteException;
    public RiskProfile searchRiskProfileById(RiskProfile theRiskProfile) throws RemoteException;
    public List<RiskProfile> retrieveAllRiskProfiles() throws RemoteException;
}
