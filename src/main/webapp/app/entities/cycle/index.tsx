import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cycle from './cycle';
import CycleDetail from './cycle-detail';
import CycleUpdate from './cycle-update';
import CycleDeleteDialog from './cycle-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CycleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CycleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CycleDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cycle} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CycleDeleteDialog} />
  </>
);

export default Routes;
