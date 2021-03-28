import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Routine from './routine';
import RoutineDetail from './routine-detail';
import RoutineUpdate from './routine-update';
import RoutineDeleteDialog from './routine-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RoutineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RoutineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RoutineDetail} />
      <ErrorBoundaryRoute path={match.url} component={Routine} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RoutineDeleteDialog} />
  </>
);

export default Routes;
