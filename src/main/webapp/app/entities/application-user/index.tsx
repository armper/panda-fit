import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ApplicationUser from './application-user';
import ApplicationUserDetail from './application-user-detail';
import ApplicationUserUpdate from './application-user-update';
import ApplicationUserDeleteDialog from './application-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApplicationUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApplicationUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApplicationUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={ApplicationUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ApplicationUserDeleteDialog} />
  </>
);

export default Routes;
