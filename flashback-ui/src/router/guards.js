const guards = (to) => {
    const isAuthenticated = localStorage.isAuthenticated;
    const role = localStorage.role;
    const admin = "ADMIN";
    const user = "USER";
    if(to.meta.requiresAuth && !isAuthenticated && to.name != 'sign-in' 
        && to.name != 'sign-up' && to.name != 'welcome'){
      return {name:'sign-in'}
    }else if ((isAuthenticated && to.meta.role == admin && role.includes(user)) || 
    (isAuthenticated && to.meta.role == user && role.includes(admin))){
        return {name:'forbidden'}
    }
  };

  export default guards;