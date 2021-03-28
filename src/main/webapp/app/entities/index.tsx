import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Routine from './routine';
import ApplicationUser from './application-user';
import Excercise from './excercise';
import Cycle from './cycle';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}routine`} component={Routine} />
      <ErrorBoundaryRoute path={`${match.url}application-user`} component={ApplicationUser} />
      <ErrorBoundaryRoute path={`${match.url}excercise`} component={Excercise} />
      <ErrorBoundaryRoute path={`${match.url}cycle`} component={Cycle} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
