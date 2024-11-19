package com.banking.service;

import java.util.List;

import com.banking.entities.Branch;

public interface BranchService {

    Branch saveBranch(Branch branch) ;

    Branch updateBranch(Integer branchId, Branch branch);

    List<Branch> getAllBranches();

    Branch getBranchById(Integer branchId);

    int deleteBranch(Integer branchId);
}

