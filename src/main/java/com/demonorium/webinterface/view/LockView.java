package com.demonorium.webinterface.view;

public class LockView {
    private Boolean lockEdit;
    private Boolean lockDelete;
    private Boolean lockShare;

    public Boolean getLockEditInverse() {
        return lockEditInverse;
    }

    public void setLockEditInverse(Boolean lockEditInverse) {
        this.lockEditInverse = lockEditInverse;
    }

    public Boolean getLockDeleteInverse() {
        return lockDeleteInverse;
    }

    public void setLockDeleteInverse(Boolean lockDeleteInverse) {
        this.lockDeleteInverse = lockDeleteInverse;
    }

    public Boolean getLockShareInverse() {
        return lockShareInverse;
    }

    public void setLockShareInverse(Boolean lockShareInverse) {
        this.lockShareInverse = lockShareInverse;
    }

    private Boolean lockEditInverse;
    private Boolean lockDeleteInverse;
    private Boolean lockShareInverse;

    public LockView() {
    }

    public LockView(Boolean lockEdit, Boolean lockDelete, Boolean lockShare) {
        this.setLockEdit(lockEdit);
        this.setLockDelete(lockDelete);
        this.setLockShare(lockShare);
    }

    public Boolean getLockEdit() {
        return lockEdit;
    }

    public void setLockEdit(Boolean lockEdit) {
        lockEditInverse = !lockEdit;
        this.lockEdit = lockEdit;
    }

    public Boolean getLockDelete() {
        return lockDelete;
    }

    public void setLockDelete(Boolean lockDelete) {
        lockDeleteInverse = !lockDelete;
        this.lockDelete = lockDelete;
    }

    public Boolean getLockShare() {
        return lockShare;
    }

    public void setLockShare(Boolean lockShare) {
        lockShareInverse = !lockShare;
        this.lockShare = lockShare;
    }
}
