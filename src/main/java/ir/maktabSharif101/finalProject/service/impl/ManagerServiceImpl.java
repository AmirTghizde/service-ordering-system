package ir.maktabSharif101.finalProject.service.impl;

import ir.maktabSharif101.finalProject.entity.Manager;
import ir.maktabSharif101.finalProject.repository.ManagerRepository;
import ir.maktabSharif101.finalProject.service.ManagerService;
import ir.maktabSharif101.finalProject.service.base.BaseUserServiceImpl;

public class ManagerServiceImpl extends BaseUserServiceImpl<Manager, ManagerRepository>
implements ManagerService {
    public ManagerServiceImpl(ManagerRepository baseRepository) {
        super(baseRepository);
    }
}
